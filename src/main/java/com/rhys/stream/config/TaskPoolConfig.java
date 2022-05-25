package com.rhys.stream.config;

import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author Rhys.Ni
 * @version 1.0
 * @date 2022/5/26 2:48 上午
 */
@Configuration
@EnableAsync
public class TaskPoolConfig implements AsyncConfigurer {
    @Bean
    public Executor taskExecutor() {
        //配置异步任务使用的线程池，推荐使用ThreadPoolTaskExecutor，不配置默认使用的是SimpleAsyncTaskExecutor
        ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
        // 设置核心线程数
        taskExecutor.setCorePoolSize(128);
        // 设置最大线程数
        taskExecutor.setMaxPoolSize(512);
        // 设置队列容量
        taskExecutor.setQueueCapacity(65536);
        // 设置线程活跃时间（秒）
        taskExecutor.setKeepAliveSeconds(60);
        // 设置默认线程名称
        taskExecutor.setThreadNamePrefix("taskExecutor-");
        // 设置拒绝策略
        taskExecutor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        return taskExecutor;
    }

    public Executor getAsyncExecutor() {
        return null;
    }

    public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
        return null;
    }
}
