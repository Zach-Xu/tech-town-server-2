package com.tech.service;

import com.tech.dto.AnswerDTO;
import com.tech.model.Vote;
import com.tech.vo.QuestionResponse;
import com.tech.vo.ResponseResult;
import com.tech.model.Question;

import java.util.List;

public interface QuestionService {

    ResponseResult<List<QuestionResponse>> getAllQuestions();

    ResponseResult<Question> createQuestion(Question question);

    ResponseResult<Question> getQuestion(Long questionId);

    ResponseResult<Vote> voteQuestion(Long questionId, Vote vote);
    void cancelVote(Question question, int previousVoteStatus) ;
    void upVote(Question question, int previousVoteStatus) ;
    void downVote(Question question, int previousVoteStatus) ;
    void cancelPreviousVoteAction(Question question, int previousVoteStatus);

    ResponseResult<Vote> getUserVoteOnQuestion(Long questionId);
}
