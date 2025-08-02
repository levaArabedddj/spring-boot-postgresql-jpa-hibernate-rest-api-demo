package com.example.postgresdemo.Service;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

@Configuration
public class RedisConfig {

    @Bean
    public JedisPool jedisPool() {
        JedisPoolConfig config = new JedisPoolConfig();
        config.setMaxTotal(8);
        config.setJmxEnabled(false);

        String redisHost = System.getenv("REDIS_HOST");
        int redisPort = Integer.parseInt(System.getenv("REDIS_PORT"));

        return new JedisPool(config, redisHost, redisPort);
    }
}
