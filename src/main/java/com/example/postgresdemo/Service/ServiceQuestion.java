package com.example.postgresdemo.Service;

import com.example.postgresdemo.DTO.DtoQuestion;
import com.example.postgresdemo.model.Question;
import com.example.postgresdemo.repository.QuestionRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.Random;

@Service
public class ServiceQuestion {

    private final QuestionRepository questionRepository;

    @Autowired
    private JedisPool jedisPool;
    ObjectMapper mapper = new ObjectMapper();

    private static final int TTL = 3600;

    @Autowired
    public ServiceQuestion(QuestionRepository questionRepository) {
        this.questionRepository = questionRepository;
    }

    public DtoQuestion randomQuestion() {
        long count = questionRepository.count();
        if (count == 0){
            return null;
        }
        long min = 1200;
        long max = 1251;

        long random = min + (long) (Math.random() * ((max - min) + 1));
        return getCachedQuestion(random);
    }

    public DtoQuestion getQuestion(Long id) {
        return questionRepository.findById(id).
                map(qst -> new DtoQuestion(qst.getTitle(),
                        qst.getDescription())).orElse(null);
    }

    public DtoQuestion getCachedQuestion(Long id) {
        try(Jedis jedis = jedisPool.getResource()) {

            String key = String.format("article:%d",id);
            String raw = jedis.get(key);
            if(raw != null) {
                return mapper.readValue(raw,DtoQuestion.class);
            }
            var article = getQuestion(id);
            if(article == null) {
                return null;
            }

            jedis.setex(key,TTL , mapper.writeValueAsString(article));
            return article;
        } catch (JsonMappingException e) {
            throw new RuntimeException(e);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
