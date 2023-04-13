package com.tech.service.impl;

import com.nimbusds.oauth2.sdk.http.HTTPResponse;
import com.tech.model.Skill;
import com.tech.repo.UserRepository;
import com.tech.service.UserService;
import com.tech.vo.ResponseResult;
import com.tech.vo.SearchUserResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public ResponseResult getUsersByKeyword(String keyword) {
        Page<Object[]> userResultPage = userRepository.findProfileWithUserByUsernameContains(keyword, Pageable.ofSize(20));
        // Extract the content from the search results page
        List<Object[]> resultList = userResultPage.getContent();
        // Create a new list to hold the search user responses
        List<SearchUserResponse> searchUserList = new ArrayList<>();
        
        // Iterate over the search results and create a new search user response for each one
        for (Object[] result : resultList) {
            SearchUserResponse searchUserResponse = new SearchUserResponse();
            searchUserResponse.setId(Long.valueOf(String.valueOf(result[0])));
            searchUserResponse.setUsername((String) result[1]);
            searchUserResponse.setAvatar((String) result[2]);
            searchUserResponse.setCreatedTime(((Timestamp) result[3]).toLocalDateTime());
            searchUserResponse.setBio((String) result[4]);

            if (Objects.nonNull(result[5])) {
                String[] skillNames = ((String) result[5]).split(",");
                searchUserResponse.setSkills(skillNames);
            }
            searchUserList.add(searchUserResponse);
        }

        return new ResponseResult(HTTPResponse.SC_OK, "Search users successfully", searchUserList);
    }
}
