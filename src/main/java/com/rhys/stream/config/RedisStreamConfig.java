package com.rhys.stream.config;

import com.rhys.stream.listener.StreamConsumerListener;
import lombok.extern.slf4j.Slf4j;
import lombok.var;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableMBeanExport;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.stream.Consumer;
import org.springframework.data.redis.connection.stream.ReadOffset;
import org.springframework.data.redis.connection.stream.StreamOffset;
import org.springframework.data.redis.stream.StreamMessageListenerContainer;
import org.springframework.data.redis.stream.Subscription;
import org.springframework.jmx.support.RegistrationPolicy;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Rhys.Ni
 * @version 1.0
 * @date 2022/5/26 4:31 上午
 */
@Configuration
@EnableCaching
@EnableMBeanExport(registration = RegistrationPolicy.IGNORE_EXISTING)
@Slf4j
public class RedisStreamConfig extends CachingConfigurerSupport {

    @Value("${spring.redis.host}")
    private String host;

    @Value("${spring.redis.port}")
    private int port;

    @Value("${spring.redis.timeout}")
    private int timeout;

    @Value("${spring.redis.password}")
    private String password;

    @Value("${redis-stream.names}")
    private String[] redisStreamNames;

    @Value("${redis-stream.groups}")
    private String[] groups;

    @Autowired
    private final StreamConsumerListener streamListener;

    public RedisStreamConfig(StreamConsumerListener streamListener) {
        this.streamListener = streamListener;
    }


    @Bean
    public JedisPool redisPoolFactory() {
        JedisPoolConfig config = new JedisPoolConfig();
        JedisPool jedisPool = new JedisPool(config, host, port, timeout, password);
        return jedisPool;
    }

    @Bean
    public List<Subscription> subscription(RedisConnectionFactory factory) {
        List<Subscription> resultList = new ArrayList<>();
        var options = StreamMessageListenerContainer
                .StreamMessageListenerContainerOptions
                .builder()
                .pollTimeout(Duration.ofSeconds(1))
                .build();
        for (String redisStreamName : redisStreamNames) {
            var listenerContainer = StreamMessageListenerContainer.create(factory, options);
            Subscription subscription = listenerContainer.receiveAutoAck(Consumer.from(groups[0], this.getClass().getName()),
                    StreamOffset.create(redisStreamName, ReadOffset.lastConsumed()), streamListener);
            resultList.add(subscription);
            listenerContainer.start();
        }
        return resultList;
    }
}
