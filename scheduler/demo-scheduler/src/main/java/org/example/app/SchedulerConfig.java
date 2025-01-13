package org.example.app;

import lombok.extern.slf4j.Slf4j;
import org.example.app.v1.tasks.V1Scheduler;
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
    public IScheduler scheduler() {
        IScheduler scheduler = new V1Scheduler();
        log.info(scheduler.getClass().getName());

        return scheduler;
    }
}
