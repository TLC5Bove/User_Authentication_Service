package com.bovetlc.user_authentication_service.repository;

import com.bovetlc.user_authentication_service.entity.Portfolio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PortfolioRespository extends JpaRepository<Portfolio, Long> {
}
