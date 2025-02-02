package org.example.order.persistence;

import org.example.order.persistence.entity.OrderProduct;
import org.example.order.persistence.entity.OrderProductId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderProductRepository extends JpaRepository<OrderProduct, OrderProductId> {
}
