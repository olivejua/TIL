package dev.oliveua.concurrencytest.persistence;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Setter
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "products")
@Entity
public class ProductEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long stock;

    private LocalDateTime createdAt;

    @Builder
    public ProductEntity(Long id, Long stock, LocalDateTime createdAt) {
        this.id = id;
        this.stock = stock;
        this.createdAt = createdAt;
    }
}
