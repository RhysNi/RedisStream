package com.rhys.stream.controller;

import com.rhys.stream.util.RedisStreamMQ;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import redis.clients.jedis.StreamEntryID;

import java.util.Map;

/**
 * @author Rhys.Ni
 * @version 1.0
 * @date 2022/5/26 2:48 上午
 */
@RestController
@RequestMapping("/stream")
public class StreamController {

    @Autowired
    private RedisStreamMQ redisStreamMQ;

    @PostMapping(value = "/send/{key}")
    public StreamEntryID send(@PathVariable String key, @RequestBody Map<String, String> message) {
        return redisStreamMQ.produce(key, message);
    }
}
