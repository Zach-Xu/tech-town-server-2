package com.tech.controller;

import com.tech.dto.AnswerDTO;
import com.tech.model.Answer;
import com.tech.model.Vote;
import com.tech.service.AnswerService;
import com.tech.vo.QuestionResponse;
import com.tech.vo.ResponseResult;
import com.tech.model.Question;
import com.tech.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


import java.util.List;


@RestController
@RequestMapping(path = "/api/questions")
public class QuestionController {

    @Autowired
    private QuestionService questionService;

    @Autowired
    private AnswerService answerService;

    @GetMapping
    public ResponseResult<List<QuestionResponse>> getAllQuestions(){
        return questionService.getAllQuestions();
    }

    @PostMapping
    public ResponseResult<Question> createQuestion(@RequestBody Question question) {
        return questionService.createQuestion(question);
    }

    @GetMapping("/{questionId}")
    public ResponseResult<Question> getQuestion(@PathVariable("questionId") Long questionId){
        return questionService.getQuestion(questionId);
    }

    @PostMapping("/answers/{questionId}")
    public ResponseResult<Answer> createAnswer(@PathVariable("questionId") Long questionId, @RequestBody AnswerDTO answer){
        return answerService.createAnswer(questionId, answer);
    }

    @PostMapping("/{questionId}/vote")
    public ResponseResult<Vote> voteQuestion(@PathVariable("questionId") Long questionId, @RequestBody Vote vote){
        return questionService.voteQuestion(questionId, vote);
    }

    @GetMapping("/{questionId}/vote")
    public ResponseResult<Vote> getUserVoteOnQuestion(@PathVariable("questionId") Long questionId) {
        return questionService.getUserVoteOnQuestion(questionId);
    }



}
