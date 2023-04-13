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
import java.util.Map;


@RestController
@RequestMapping(path = "/api/questions")
public class QuestionController {

    @Autowired
    private QuestionService questionService;

    @Autowired
    private AnswerService answerService;

    @GetMapping
    public ResponseResult<List<QuestionResponse>> getAllQuestions(@RequestParam(required = false) String sort){
        return questionService.getAllQuestions(sort);
    }

    @GetMapping("/search")
    public ResponseResult<List<QuestionResponse>> getSearchQuestion(@RequestParam(required = false) String tag, @RequestParam(required = false) String keyword){
        return questionService.getSearchQuestion(tag, keyword);
    }

    @PostMapping
    public ResponseResult<Question> createQuestion(@RequestBody Question question) {
        return questionService.createQuestion(question);
    }

    @GetMapping("/{questionId}")
    public ResponseResult<Map<String, Object>> getQuestion(@PathVariable("questionId") Long questionId){
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

    @GetMapping("/bookmark")
    public ResponseResult<List<QuestionResponse>> getBookmarkedQuestions(@RequestParam(name = "userId", required = false) Long userId){
        return questionService.getUserBookmarkedQuestions(userId);
    }

    @PostMapping("/bookmark/{questionId}")
    public ResponseResult bookmarkQuestion(@PathVariable("questionId") Long questionId){
        return questionService.bookmarkQuestion(questionId);
    }

    @DeleteMapping("/bookmark/{questionId}")
    public ResponseResult unBookmarkQuestion(@PathVariable("questionId") Long questionId){
        return questionService.unBookmarkQuestion(questionId);
    }


}
