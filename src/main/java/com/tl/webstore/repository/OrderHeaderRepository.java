package com.tl.webstore.repository;

import com.tl.webstore.domain.OrderHeader;

import com.tl.webstore.domain.UserProfile;
import com.tl.webstore.domain.enumeration.OrderType;
import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the OrderHeader entity.
 */
public interface OrderHeaderRepository extends JpaRepository<OrderHeader,Long> {

    List<OrderHeader> findByType(OrderType type);

    OrderHeader findByTypeAndCookie(OrderType type, String cookie);
}
