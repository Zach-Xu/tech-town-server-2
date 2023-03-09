package com.tech.controller;

import com.tech.dto.AnswerDTO;
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

    @GetMapping
    public ResponseResult<List<QuestionResponse>> getAllQuestions(){
        return questionService.getAllQuestions();
    }

    @PostMapping
    public ResponseResult<Question> createQuestion(@RequestBody Question question) {
        return questionService.createQuestion(question);
    }

    @GetMapping("/{id}")
    public ResponseResult<Question> getQuestion(@PathVariable("id") Long questionId){
        return questionService.getQuestion(questionId);
    }

    @PostMapping("/answers/{questionId}")
    public ResponseResult<Question> createAnswer(@PathVariable("questionId") Long questionId, @RequestBody AnswerDTO answer){
        return questionService.createAnswer(questionId, answer);
    }

}
