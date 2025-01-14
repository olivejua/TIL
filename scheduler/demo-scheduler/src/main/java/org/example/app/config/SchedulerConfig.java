package org.example.app.config;

import lombok.extern.slf4j.Slf4j;
import org.example.app.common.IScheduler;
import org.example.app.v3.V3Scheduler;
import org.example.app.v3.V3TaskService;
import org.redisson.api.RedissonClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

@Slf4j
@Configuration
public class SchedulerConfig {

    @Bean
    public ThreadPoolTaskScheduler taskScheduler() {
        ThreadPoolTaskScheduler scheduler = new ThreadPoolTaskScheduler();
        scheduler.setPoolSize(10);
        scheduler.initialize();

        return scheduler;
    }

    @Bean
    public IScheduler scheduler(V3TaskService taskService, RedissonClient redissonClient) {
        IScheduler scheduler = new V3Scheduler(taskService, redissonClient);
        log.info(scheduler.getClass().getName());

        return scheduler;
    }
}
