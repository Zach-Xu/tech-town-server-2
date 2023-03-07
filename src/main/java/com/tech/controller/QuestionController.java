package com.tech.controller;

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
    public ResponseResult<List<Question>> getAllQuestions(){
        return questionService.getAllQuestions();
    }

    @PostMapping
    public ResponseResult<Question> createQuestion(@RequestBody Question question) {
        return questionService.createQuestion(question);
    }


}
