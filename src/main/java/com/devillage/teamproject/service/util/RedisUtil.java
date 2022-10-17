package com.devillage.teamproject.service.util;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
@RequiredArgsConstructor
public class RedisUtil {
    private final StringRedisTemplate redisTemplate;

//    public void setData(String key,String value){
//        ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();
//        valueOperations.set(key,value);
//    }

    public String getData(String key) {
        ValueOperations<String,String> valueOperations = redisTemplate.opsForValue();
        return valueOperations.get(key);
    }

    public void setDataExpire(String key,String value,Long duration){
        ValueOperations<String,String> valueOperations = redisTemplate.opsForValue();
        Duration expireDuration = Duration.ofSeconds(duration);
        valueOperations.set(key, value,expireDuration);
    }

    public void deleteData(String key) {
        redisTemplate.delete(key);
    }

}
