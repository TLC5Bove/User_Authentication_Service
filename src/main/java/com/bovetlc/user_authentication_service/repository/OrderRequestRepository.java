package com.bovetlc.user_authentication_service.repository;

import com.bovetlc.user_authentication_service.entity.OrderRequest;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRequestRepository extends JpaRepository<OrderRequest, Long> {
}
