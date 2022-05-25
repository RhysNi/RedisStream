package com.rhys.stream.listener;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.stream.MapRecord;
import org.springframework.data.redis.stream.StreamListener;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author Rhys.Ni
 * @version 1.0
 * @date 2022/5/26 1:22 上午
 */
@Component
@Slf4j
public class StreamConsumerListener implements StreamListener<String, MapRecord<String, String, String>> {
    @Resource
    private ThreadPoolTaskExecutor taskExecutor;

    @Override
    public void onMessage(final MapRecord<String, String, String> record) {
        taskExecutor.execute(() -> log.info("接收到一个消息 stream:{},id:{},value:{}", record.getStream(), record.getId().getValue(), record.getValue()));
    }
}
