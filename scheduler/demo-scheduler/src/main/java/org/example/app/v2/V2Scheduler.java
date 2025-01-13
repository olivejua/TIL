package org.example.app.v2;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.example.app.common.IScheduler;
import org.example.app.common.TaskService;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.Arrays;
import java.util.List;

/**
 * DB 테스트
 */
@Slf4j
public class V2Scheduler implements IScheduler {
    private final TaskService taskService;

    public V2Scheduler(TaskService taskService) {
        this.taskService = taskService;
    }

    @Scheduled(fixedRate = 1000)
    @Transactional
    public void test1() throws InterruptedException {
        log.info("task1 start");

        List<Long> taskIds =taskService.getTasks();
        log.info("lock taskIds {}", Arrays.toString(taskIds.toArray()));
        Thread.sleep(10_000); //10초간

        log.info("task1 end");
    }

    @Scheduled(fixedRate = 1000)
    @Transactional
    public void test2() throws InterruptedException {
        log.info("task2 start");

        List<Long> taskIds =taskService.getTasks();
        log.info("lock taskIds {}", Arrays.toString(taskIds.toArray()));
        Thread.sleep(10_000); //10초간

        log.info("task2 end");
    }

    @Scheduled(fixedRate = 1000)
    @Transactional
    public void test3() throws InterruptedException {
        log.info("task3 start");

        List<Long> taskIds =taskService.getTasks();
        log.info("lock taskIds {}", Arrays.toString(taskIds.toArray()));
        Thread.sleep(10_000); //10초간

        log.info("task3 end");
    }
}
