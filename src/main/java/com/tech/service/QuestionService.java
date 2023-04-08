package com.tech.service;

import com.tech.dto.AnswerDTO;
import com.tech.model.Vote;
import com.tech.vo.QuestionResponse;
import com.tech.vo.ResponseResult;
import com.tech.model.Question;

import java.util.List;
import java.util.Map;

public interface QuestionService {

    ResponseResult<List<QuestionResponse>> getAllQuestions();

    ResponseResult<Question> createQuestion(Question question);

    ResponseResult<Map<String,Object>> getQuestion(Long questionId);

    ResponseResult<Vote> voteQuestion(Long questionId, Vote vote);
    void cancelVote(Question question, int previousVoteStatus) ;
    void upVote(Question question, int previousVoteStatus) ;
    void downVote(Question question, int previousVoteStatus) ;
    void cancelPreviousVoteAction(Question question, int previousVoteStatus);

    ResponseResult<Vote> getUserVoteOnQuestion(Long questionId);

    ResponseResult<List<QuestionResponse>> getUserBookmarkedQuestions(Long userId);

    ResponseResult bookmarkQuestion(Long questionId);

    ResponseResult unBookmarkQuestion(Long questionId);

    List<QuestionResponse> convertToQuestionResponseList(List<Question> questions);
}
