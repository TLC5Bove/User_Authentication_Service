package com.bovetlc.user_authentication_service.repository;

import com.bovetlc.user_authentication_service.entity.Portfolio;
import com.bovetlc.user_authentication_service.entity.UserStock;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserStockRepository extends JpaRepository<UserStock, Long> {

    Optional<List<UserStock>> findAllByPortfolio(Portfolio portfolio);
}
