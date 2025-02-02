package org.example.order.persistence.entity;

import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Embeddable
public class OrderProductId implements Serializable {
    private Long orderId;
    private Long productId;
}
