package com.example.postgresdemo.repository;

import com.example.postgresdemo.model.Answer;
import com.example.postgresdemo.model.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuestionRepository extends JpaRepository<Question, Long> {
    List<Question> findByTitleContainingIgnoreCase(String title);
}
