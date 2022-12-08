package com.bovetlc.user_authentication_service.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
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
    public Long id;
    public String portfolioName;
    public Double portfolioValue;
    public Boolean isActive;
    @ManyToOne
    @JoinColumn(
            nullable = false,
            name = "user_id"
    )
    public User user;
    @OneToMany
    @JoinColumn(
            nullable = false,
            name = "user_stock_id"
    )
    public List<UserStock> stocks;

    public Portfolio(String portfolioName, User user) {
        this.portfolioName = portfolioName;
        this.user = user;
        this.portfolioValue = 0.0;
        this.isActive = true;
        this.stocks = new ArrayList<>();
    }
}
