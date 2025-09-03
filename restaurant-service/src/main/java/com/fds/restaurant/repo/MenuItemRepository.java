package com.fds.restaurant.repo;

import com.fds.restaurant.domain.MenuItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MenuItemRepository extends JpaRepository<MenuItem, Long> {
    List<MenuItem> findByRestaurant_Id(Long restaurantId);
}

