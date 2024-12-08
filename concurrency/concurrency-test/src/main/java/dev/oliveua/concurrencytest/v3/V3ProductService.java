package dev.oliveua.concurrencytest.v3;

import dev.oliveua.concurrencytest.error.InsufficientStockException;
import dev.oliveua.concurrencytest.persistence.ProductEntity;
import dev.oliveua.concurrencytest.persistence.ProductJpaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

@Slf4j
@RequiredArgsConstructor
@Service
public class V3ProductService {
    private final ProductJpaRepository productJpaRepository;
    private final RedissonClient redissonClient;

    public void purchaseProductWithRedisLock(Long productId, int quantity, int index) {
        RLock lock = redissonClient.getLock("PRODUCT_" + productId);

        try {
            if (lock.tryLock(500, 3000, TimeUnit.MILLISECONDS)) {
                try {
                    log.info("({}번째) Lock 획득 성공!", index);
                    purchaseProduct(productId, quantity, index);
                } finally {
                    lock.unlock();
                }
            } else {
                log.error("Redis Lock 획득 실패!");
                throw new RuntimeException("Redis Lock 획득 실패!");
            }
        } catch (InterruptedException e) {
            log.error("Redis Lock 획득 실패! Interrupted while acquiring lock");
            throw new RuntimeException("Interrupted while acquiring lock", e);
        }
    }

    private void purchaseProduct(Long productId, int quantity, int index) {
        ProductEntity product = productJpaRepository.findById(productId).get();
        log.info("({}번째) 상품 {}을(를) {}개 구매 시도. 잔여재고: {}", index, productId, quantity, (product.getStock() - quantity));
        if (product.getStock() >= quantity) {
            product.setStock(product.getStock() - quantity);
            productJpaRepository.save(product);
        } else {
            log.error("재고부족! {}번째 요청", index);
            throw new InsufficientStockException("재고부족!");
        }
    }
}
