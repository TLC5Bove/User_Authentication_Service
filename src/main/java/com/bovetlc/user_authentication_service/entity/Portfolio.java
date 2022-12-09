package com.bovetlc.user_authentication_service.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter
@Setter
@NoArgsConstructor
@ToString
public class Portfolio {

    @SequenceGenerator(
            name = "portfolio_sequence",
            sequenceName = "portfolio_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "portfolio_sequence"
    )
    @Id
    private Long id;
    private String portfolioName;
    private Double portfolioValue;
    private Boolean isActive;
    @ManyToOne
    @JoinColumn(
            nullable = false,
            name = "user_id"
    )
    private User user;

    public Portfolio(String portfolioName, User user) {
        this.portfolioName = portfolioName;
        this.user = user;
        this.portfolioValue = 0.0;
        this.isActive = true;
    }
}
