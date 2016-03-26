package com.tl.webstore.repository;

import com.tl.webstore.domain.UserAddress;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the UserAddress entity.
 */
public interface UserAddressRepository extends JpaRepository<UserAddress,Long> {

}
