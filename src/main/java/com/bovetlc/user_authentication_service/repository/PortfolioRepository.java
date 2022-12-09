package com.bovetlc.user_authentication_service.repository;

import com.bovetlc.user_authentication_service.entity.Portfolio;
import com.bovetlc.user_authentication_service.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PortfolioRepository extends JpaRepository<Portfolio, Long> {

    Optional<List<Portfolio>> findAllByUser(User user);
}
