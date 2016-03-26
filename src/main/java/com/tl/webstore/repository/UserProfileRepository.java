package com.tl.webstore.repository;

import com.tl.webstore.domain.UserProfile;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the UserProfile entity.
 */
public interface UserProfileRepository extends JpaRepository<UserProfile,Long> {

}
