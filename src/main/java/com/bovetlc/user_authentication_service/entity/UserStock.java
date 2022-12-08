package com.bovetlc.user_authentication_service.entity;

import com.bovetlc.user_authentication_service.entity.enums.Ticker;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Setter
@Getter
@NoArgsConstructor
@ToString
public class UserStock{

    @Id
    @SequenceGenerator(
            name = "user_stock_sequence",
            sequenceName = "user_stock_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "user_stock_sequence"
    )
    private Long id;
    @Enumerated(EnumType.STRING)
    private Ticker product;
    private Integer quantity;
    @ManyToOne
    @JoinColumn(
            nullable = false,
            name = "user_id"
    )
    private User user;

    public UserStock(Ticker product, Integer quantity, User user) {
        this.product = product;
        this.quantity = quantity;
        this.user = user;
    }
}
