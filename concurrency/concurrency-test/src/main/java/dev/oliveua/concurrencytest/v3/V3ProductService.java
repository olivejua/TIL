package dev.oliveua.concurrencytest.v3;

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
public class V3ProductService {
    private final ProductJpaRepository productJpaRepository;

    public void purchaseProductWithRedisLock(Long productId, int quantity, int index) {

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
