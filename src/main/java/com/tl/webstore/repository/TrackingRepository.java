package com.tl.webstore.repository;

import com.tl.webstore.domain.Tracking;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Tracking entity.
 */
public interface TrackingRepository extends JpaRepository<Tracking,Long> {

}
