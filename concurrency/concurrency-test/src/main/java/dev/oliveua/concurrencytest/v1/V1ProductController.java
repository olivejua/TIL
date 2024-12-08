package dev.oliveua.concurrencytest.v1;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Slf4j
@RequestMapping("/v1")
@RequiredArgsConstructor
@RestController
public class V1ProductController {
    private final V1ProductService v1ProductService;

    @PutMapping("/products/{id}/purchase")
    public void concurrencyIssue(@PathVariable Long id) throws InterruptedException {
        long start = System.currentTimeMillis();

        int threadCount = 120;
        ExecutorService executorService = Executors.newFixedThreadPool(threadCount);
        CountDownLatch latch = new CountDownLatch(threadCount);

        for (int i = 1; i <= threadCount; i++) {
            int finalI = i;
            executorService.submit(() -> {
                try {
                    log.info("{}번째 요청", finalI);
                    final int quantity = 10;
                    v1ProductService.purchaseProduct(id, quantity, finalI);
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await();
        executorService.shutdown();

        long end = System.currentTimeMillis();
        log.info("소요시간: {}ms", (end - start));
    }
}
