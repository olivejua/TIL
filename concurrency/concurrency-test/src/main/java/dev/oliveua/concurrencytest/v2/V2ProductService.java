package dev.oliveua.concurrencytest.v2;

import dev.oliveua.concurrencytest.error.InsufficientStockException;
import dev.oliveua.concurrencytest.persistence.ProductEntity;
import dev.oliveua.concurrencytest.persistence.ProductJpaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantLock;

@Slf4j
@RequiredArgsConstructor
@Service
public class V2ProductService {
    private final ProductJpaRepository productJpaRepository;
    private final Map<Long, ReentrantLock> lockMap = new ConcurrentHashMap<>();

    public void purchaseProductWithApplicationLock(Long productId, int quantity, int index) {
        lockMap.putIfAbsent(productId, new ReentrantLock());
        ReentrantLock lock = lockMap.get(productId);

        lock.lock();
        try {
            log.info("{}번째 요청이 Lock 획득", index);
            purchaseProduct(productId, quantity, index);
        } finally {
            lock.unlock();
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
