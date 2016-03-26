package com.tl.webstore.repository;

import com.tl.webstore.domain.UserLogin;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the UserLogin entity.
 */
public interface UserLoginRepository extends JpaRepository<UserLogin,Long> {

}
