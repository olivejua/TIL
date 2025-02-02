package org.example.order.persistence.entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "order_products")
@Entity
public class OrderProduct {

    @EmbeddedId
    private OrderProductId id;

    @MapsId("orderId")
    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;

    private Integer amount;

    private Integer quantity;
}
