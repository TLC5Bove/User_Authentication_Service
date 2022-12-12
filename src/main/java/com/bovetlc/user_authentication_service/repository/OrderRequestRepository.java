package com.bovetlc.user_authentication_service.repository;

import com.bovetlc.user_authentication_service.entity.OrderRequest;
import com.bovetlc.user_authentication_service.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRequestRepository extends JpaRepository<OrderRequest, Long> {
    Optional<List<OrderRequest>> findAllByUser(User user);

    Optional<OrderRequest> findByOsId(String osid);
}
