package com.example.postgresdemo.controller;

import com.example.postgresdemo.DTO.DtoQuestion;
import com.example.postgresdemo.Service.ServiceQuestion;
import com.example.postgresdemo.exception.ResourceNotFoundException;
import com.example.postgresdemo.model.Answer;
import com.example.postgresdemo.model.Question;
import com.example.postgresdemo.repository.AnswerRepository;
import com.example.postgresdemo.repository.QuestionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import java.util.List;

@RestController
public class QuestionController {

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private AnswerRepository answerRepository;

    @Autowired
    private ServiceQuestion serviceQuestion;

    @GetMapping("/questions")
    public Page<Question> getQuestions(Pageable pageable) {
        return questionRepository.findAll(pageable);
    }


    @PostMapping("/questions")
    public Question createQuestion(@Valid @RequestBody Question question) {
        return questionRepository.save(question);
    }

    @PutMapping("/questions/{questionId}")
    public Question updateQuestion(@PathVariable Long questionId,
                                   @Valid @RequestBody Question questionRequest) {
        return questionRepository.findById(questionId)
                .map(question -> {
                    question.setTitle(questionRequest.getTitle());
                    question.setDescription(questionRequest.getDescription());
                    return questionRepository.save(question);
                }).orElseThrow(() -> new ResourceNotFoundException("Question not found with id " + questionId));
    }


    @DeleteMapping("/questions/{questionId}")
    public ResponseEntity<?> deleteQuestion(@PathVariable Long questionId) {
        return questionRepository.findById(questionId)
                .map(question -> {
                    questionRepository.delete(question);
                    return ResponseEntity.ok().build();
                }).orElseThrow(() -> new ResourceNotFoundException("Question not found with id " + questionId));
    }

    @GetMapping("/getAllAnswer/{questionId}")
    public List<Answer> getAllAnswersOnQuestion(
            @PathVariable Long questionId) {
        return answerRepository.findAllByQuestionId(questionId);
    }

    @GetMapping("/getQuestionByTitle")
    public List<Question> getQuestionByTitle(
            @RequestParam String keyword) {
        return questionRepository.findByTitleContainingIgnoreCase(keyword);
    }

    @DeleteMapping("/deleteQuestionByTitle")
    public ResponseEntity<?> deleteQuestionByTitle(
            @RequestParam String keyword) {
        return questionRepository.findByTitleContainingIgnoreCase(keyword)
                .stream()
                .peek(questionRepository::delete)
                .findAny()
                .map(question -> ResponseEntity.ok().build())
                .orElseThrow(() ->
                        new ResourceNotFoundException
                                ("Question not found with key Word " + keyword));
    }

    @GetMapping("/randomQuestion")
    public DtoQuestion getRandomQuestion() {
        return serviceQuestion.randomQuestion();
    }


    @GetMapping("/questionCached/{id}")
    public DtoQuestion getCachedQuestion(@PathVariable Long id) {
        return serviceQuestion.getCachedQuestion(id);
    }

}
