package com.fpt_be.fpt_be.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.fpt_be.fpt_be.Entity.OrderItem;
import java.util.List;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
    List<OrderItem> findByOrderId(Long orderId);
    
} 