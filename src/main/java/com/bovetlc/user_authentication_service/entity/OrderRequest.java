package com.bovetlc.user_authentication_service.entity;

import com.bovetlc.user_authentication_service.entity.enums.OrderType;
import com.bovetlc.user_authentication_service.entity.enums.Side;
import com.bovetlc.user_authentication_service.entity.enums.Status;
import com.bovetlc.user_authentication_service.entity.enums.Ticker;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.Hibernate;

import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class OrderRequest {
    // TODO 1. CREATE ORDER REQUEST TO SEND ORDER
    // TODO 2. RUN VALIDATIONS ON THE ORDERS
    // TODO 3. SEND ORDER TO OPS IF VALIDATION IS SUCCESSFUL
    // TODO 4. SAVE ORDER REQUEST TO DB IF RESPONSE IS A SUCCESS
    @Id
    @SequenceGenerator(
            name = "request_sequence",
            sequenceName = "request_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "request_sequence"
    )
    private Long id;
    private Ticker ticker;
    private Integer quantity;
    private Double price;
    private Side side;
    private OrderType type;
    private LocalDateTime orderDate;
    private Status status;
    private String OSID;
    @ManyToOne
    @JoinColumn(
            nullable = false,
            name = "user_id"
    )
    private User user;

    @ManyToOne
    @JoinColumn(
            nullable = false,
            name = "port_id"
    )
    private Portfolio portfolio;

    public OrderRequest(Ticker ticker, Integer quantity, Double price, Side side, OrderType type, User user, String OSID, Portfolio portfolio) {
        this.ticker = ticker;
        this.quantity = quantity;
        this.price = price;
        this.side = side;
        this.type = type;
        this.user = user;
        this.OSID = OSID;
        this.portfolio = portfolio;
        this.orderDate = LocalDateTime.now();
        this.status = Status.PENDING;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        OrderRequest that = (OrderRequest) o;
        return id != null && Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
