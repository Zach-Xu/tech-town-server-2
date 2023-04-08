package com.tech.service.impl;

import com.nimbusds.oauth2.sdk.http.HTTPResponse;
import com.tech.config.GitHubConfig;
import com.tech.dto.ProfileDTO;
import com.tech.exception.NotFoundException;
import com.tech.model.Profile;
import com.tech.model.Skill;
import com.tech.model.User;
import com.tech.repo.*;
import com.tech.service.ProfileService;
import com.tech.utils.Constants;
import com.tech.utils.FollowStatus;
import com.tech.utils.StringUtils;
import com.tech.vo.ProfileResponse;
import com.tech.vo.ResponseResult;
import com.tech.vo.UserCardResponse;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import javax.net.ssl.SSLContext;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

@Service
public class ProfileServiceImpl implements ProfileService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private FollowRepository followRepository;

    @Autowired
    private ProfileRepository profileRepository;

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private AnswerRepository answerRepository;

    @Resource
    private RestTemplate restTemplate;


    @Override
    public ResponseResult<ProfileResponse> getUserProfile(Long userId) {
        User loginUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("User not found: " + userId));

        // get user profile, create one if not present in database
        Profile profile = profileRepository.findByUserId(userId).orElseGet(() -> {
                    Profile p = new Profile(userId);
                    profileRepository.save(p);
                    return p;
                }
        );


        ProfileResponse profileResponse = new ProfileResponse();
        BeanUtils.copyProperties(profile, profileResponse);

        profileResponse.setJoinTime(user.getCreatedTime());
        profileResponse.setQuestions(questionRepository.countByUser_Id(userId).intValue());
        profileResponse.setUsername(user.getUsername());
        profileResponse.setAnswers(answerRepository.countByUser_Id(userId).intValue());
        profileResponse.setFollowers(followRepository.countByUserIdAndStatus(userId, FollowStatus.FOLLOW).intValue());
        profileResponse.setFollowing(followRepository.countByFollowerIdAndStatus(userId, FollowStatus.FOLLOW).intValue());
        profileResponse.setAvatar(user.getAvatar());

        // fetch profile for loggedIn user
        if (loginUser.getId().equals(userId)) {
            profileResponse.setFollowed(false);
        } else {
            profileResponse.setFollowed(followRepository.existsByUserIdAndFollowerIdAndStatus(userId, loginUser.getId(), FollowStatus.FOLLOW));
        }
        return new ResponseResult<>(HTTPResponse.SC_OK, "fetched user profile successfully", profileResponse);
    }

    @Override
    public ResponseResult<UserCardResponse> getUserCardInfo(Long userId) {
        User loginUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("User not found: " + userId));

        UserCardResponse userCardResponse = new UserCardResponse();
        userCardResponse.setFollowing(followRepository.countByFollowerIdAndStatus(userId, FollowStatus.FOLLOW).intValue());
        userCardResponse.setFollowers(followRepository.countByUserIdAndStatus(userId, FollowStatus.FOLLOW).intValue());
        userCardResponse.setQuestions(questionRepository.countByUser_Id(userId).intValue());
        userCardResponse.setAnswers(answerRepository.countByUser_Id(userId).intValue());
        userCardResponse.setAvatar(user.getAvatar());

        // fetch user card for loggedIn user
        if (loginUser.getId().equals(userId)) {
            userCardResponse.setFollowed(false);
        } else {
            userCardResponse.setFollowed(followRepository.existsByUserIdAndFollowerIdAndStatus(userId, loginUser.getId(), FollowStatus.FOLLOW));
        }

        return new ResponseResult<>(HTTPResponse.SC_OK, "fetched user card successfully", userCardResponse);
    }

    @Override
    public ResponseResult getUserRepos(String username) {
        String url = "https://api.github.com/users/{1}/repos?per_page=5&sort=created:desc";
        // send http request to GitHub api to fetch repos info
        ResponseEntity<Object[]> response = restTemplate.getForEntity(url, Object[].class, username);
        String message = response.getStatusCodeValue() == HTTPResponse.SC_OK ? "Fetched GitHub Repos successfully" : "Failed to Fetched GitHub Repos, possible reason: username not found";
        return new ResponseResult(response.getStatusCodeValue(), message, response.getBody());
    }

    @Override
    public ResponseResult<Profile> updateUserProfile(ProfileDTO profile) {

        // get user profile, create one if not present in database
        User loginUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long userId = loginUser.getId();


        String github = profile.getGithub();

        // validate GitHub info when it's present in request body
        if (Strings.isNotEmpty(github)) {
            // update user GitHub avatar link
            if (StringUtils.isValidGitHubUserLink(github)) {
                userRepository.findById(userId).ifPresent(user -> {
                    user.setAvatar(Constants.AVATAR_BASE_URL + StringUtils.extractGitHubUsername(github) + ".png");
                    userRepository.save(user);
                });

            } else if (StringUtils.isValidGitHubUsername((github))) {
                userRepository.findById(userId).ifPresent(user -> {
                    user.setAvatar(Constants.AVATAR_BASE_URL + github + ".png");
                    userRepository.save(user);
                });
            } else {
                throw new IllegalArgumentException("Invalid GitHub user link or username: " + github);
            }
        }else{
            userRepository.findById(userId).ifPresent(user -> {
                user.setAvatar(null);
                userRepository.save(user);
            });
        }

        // update username when it's present in request body
        if (Strings.isNotEmpty(profile.getUsername())) {
            User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("User not found: " + userId));
            user.setUsername(profile.getUsername());
            userRepository.save(user);
        }

        // fetch user profile
        Profile profileDB = profileRepository.findByUserId(userId).orElseGet(() -> new Profile(userId));

        profileDB.setGithub(github);
        profileDB.setBio(profile.getBio());

        List<Skill> currentSkills = profileDB.getSkills();
        List<Skill> skillsToAdd = profile.getSkills();
        if (skillsToAdd.isEmpty()) {
            currentSkills.clear();
        } else {
            List<String> currentSkillNames = currentSkills.stream().map(Skill::getSkillName).toList();

            // new skills are skills that do no present in current Skills
            List<Skill> newSkills = skillsToAdd.stream().
                    filter(s -> !currentSkillNames.contains(s.getSkillName())
                    ).toList();

            // filter out skills duplicated skills
            List<String> skillToAddNames = skillsToAdd.stream().map(skillToAdd -> skillToAdd.getSkillName()).toList();
            List<Skill> duplicatedSkills = currentSkills.stream().filter(s -> skillToAddNames.contains(s.getSkillName())).toList();

            // remove skills
            if (newSkills.size() == 0) {
                currentSkills.clear();
                currentSkills.addAll(duplicatedSkills);
            }

            // add new skills
            if (newSkills.size() > 0) {
                currentSkills.clear();
                currentSkills.addAll(duplicatedSkills);
                currentSkills.addAll(newSkills);
            }

            newSkills.forEach(skill -> skill.setProfile(profileDB));
        }

        profileRepository.save(profileDB);
        return new ResponseResult<>(HTTPResponse.SC_OK, "updated profile successfully", profileDB);
    }
}
