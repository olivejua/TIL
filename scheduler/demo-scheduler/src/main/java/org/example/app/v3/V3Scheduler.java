package org.example.app.v3;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.app.common.IScheduler;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 분산락 테스트
 */
@RequiredArgsConstructor
@Slf4j
public class V3Scheduler implements IScheduler {
    private final V3TaskService taskService;
    private final RedissonClient redissonClient;

    private static final String LOCK_NAME = "scheduler:find_tasks";

    @Scheduled(fixedRate = 1000)
    public void test1() throws InterruptedException {
        log.info("task1 start");

        long start = System.currentTimeMillis();

        RLock lock = redissonClient.getLock(LOCK_NAME);
        List<Long> taskIds = new ArrayList<>();
        try {
            lock.lock();
            taskIds.addAll(taskService.getTasks());
        }
        finally {
            if (lock.isHeldByCurrentThread()) {
                lock.unlock();
            }
        }

        long end = System.currentTimeMillis();

        log.info("[task1] time: {}ms / get taskIds {}", (end-start), Arrays.toString(taskIds.toArray()));
        Thread.sleep(10_000); //10초간

        log.info("task1 end");
    }

    @Scheduled(fixedRate = 1000)
    public void test2() throws InterruptedException {
        log.info("task2 start");

        long start = System.currentTimeMillis();

        RLock lock = redissonClient.getLock(LOCK_NAME);
        List<Long> taskIds = new ArrayList<>();
        try {
            lock.lock();
            taskIds.addAll(taskService.getTasks());
        }
        finally {
            if (lock.isHeldByCurrentThread()) {
                lock.unlock();
            }
        }

        long end = System.currentTimeMillis();
        log.info("[task2] time: {}ms / get taskIds {}", (end-start), Arrays.toString(taskIds.toArray()));
        Thread.sleep(10_000); //10초간

        log.info("task2 end");
    }

    @Scheduled(fixedRate = 1000)
    public void test3() throws InterruptedException {
        log.info("task3 start");

        long start = System.currentTimeMillis();

        RLock lock = redissonClient.getLock(LOCK_NAME);
        List<Long> taskIds = new ArrayList<>();
        try {
            lock.lock();
            taskIds.addAll(taskService.getTasks());
        }
        finally {
            if (lock.isHeldByCurrentThread()) {
                lock.unlock();
            }
        }

        long end = System.currentTimeMillis();
        log.info("[task3] time: {}ms / get taskIds {}", (end-start), Arrays.toString(taskIds.toArray()));
        Thread.sleep(10_000); //10초간

        log.info("task3 end");
    }
}
