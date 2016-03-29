package com.tl.webstore.repository;

import com.tl.webstore.domain.OrderHeader;
import com.tl.webstore.domain.OrderItem;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the OrderItem entity.
 */
public interface OrderItemRepository extends JpaRepository<OrderItem,Long> {

    List<OrderItem> findByOrderHeader(OrderHeader orderHeader);
}
