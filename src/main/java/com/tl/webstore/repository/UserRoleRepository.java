package com.tl.webstore.repository;

import com.tl.webstore.domain.UserRole;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the UserRole entity.
 */
public interface UserRoleRepository extends JpaRepository<UserRole,Long> {
	List<UserRole> findByUserProfileId(Long id);

}
