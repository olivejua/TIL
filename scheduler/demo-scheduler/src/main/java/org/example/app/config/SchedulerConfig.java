package org.example.app.config;

import lombok.extern.slf4j.Slf4j;
import org.example.app.common.IScheduler;
import org.example.app.common.TaskService;
import org.example.app.persistence.TaskRepository;
import org.example.app.v2.V2Scheduler;
import org.example.app.v2.V2TaskService;
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
    public IScheduler scheduler(TaskService taskService) {
        IScheduler scheduler = new V2Scheduler(taskService);
        log.info(scheduler.getClass().getName());

        return scheduler;
    }

    @Bean
    public TaskService taskService(TaskRepository taskRepository) {
        return new V2TaskService(taskRepository);
    }
}
