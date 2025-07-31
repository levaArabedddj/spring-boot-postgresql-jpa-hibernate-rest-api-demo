package com.example.postgresdemo.repository;

import com.example.postgresdemo.model.Answer;
import com.example.postgresdemo.model.Question;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface AnswerRepository extends JpaRepository<Answer, Long> {
    List<Answer> findByQuestionId(Long questionId);
    List<Answer> findAllByQuestionId(Long questionId);
}
