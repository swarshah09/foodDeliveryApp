package com.fds.restaurant.web;

import com.fds.restaurant.domain.MenuItem;
import com.fds.restaurant.domain.Restaurant;
import com.fds.restaurant.repo.MenuItemRepository;
import com.fds.restaurant.repo.RestaurantRepository;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/menus")
public class MenuController {
    private final MenuItemRepository menuItemRepository;
    private final RestaurantRepository restaurantRepository;

    public MenuController(MenuItemRepository menuItemRepository, RestaurantRepository restaurantRepository) {
        this.menuItemRepository = menuItemRepository;
        this.restaurantRepository = restaurantRepository;
    }

    @GetMapping("/restaurant/{restaurantId}")
    public List<MenuItem> byRestaurant(@PathVariable Long restaurantId) {
        return menuItemRepository.findByRestaurant_Id(restaurantId);
    }

    @PostMapping("/restaurant/{restaurantId}")
    public ResponseEntity<MenuItem> create(@PathVariable Long restaurantId, @Valid @RequestBody MenuItem item) {
        Restaurant r = restaurantRepository.findById(restaurantId).orElse(null);
        if (r == null) return ResponseEntity.notFound().build();
        item.setRestaurant(r);
        return ResponseEntity.ok(menuItemRepository.save(item));
    }
}

