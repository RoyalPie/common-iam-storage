package com.evo.iam.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;

public class TokenBlacklistService {
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;


}
