package org.example.app.v1.tasks;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalTime;

@Slf4j
@Component
public class CustomScheduler {

    // 매 1초마다 실행
    @Scheduled(fixedRate = 1000)
    public void runTask1() throws InterruptedException {
        int taskId = (int)(Math.random() * 100);
        log.info("[task1-{}] Start", taskId);
        Thread.sleep(5_000);
        log.info("[task1-{}] End", taskId);
    }

    @Scheduled(fixedRate = 1000)
    public void runTask2() throws InterruptedException {
        log.info("[task2] Execute");
    }
}
