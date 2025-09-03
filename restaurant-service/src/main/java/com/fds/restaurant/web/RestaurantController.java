package com.fds.restaurant.web;

import com.fds.restaurant.domain.Restaurant;
import com.fds.restaurant.repo.RestaurantRepository;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/restaurants")
public class RestaurantController {
    private final RestaurantRepository restaurantRepository;
    public RestaurantController(RestaurantRepository restaurantRepository) { this.restaurantRepository = restaurantRepository; }

    @GetMapping
    public List<Restaurant> list() { return restaurantRepository.findAll(); }

    @PostMapping
    public Restaurant create(@Valid @RequestBody Restaurant r) { return restaurantRepository.save(r); }

    @GetMapping("/{id}")
    public ResponseEntity<Restaurant> get(@PathVariable Long id) {
        return restaurantRepository.findById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }
}

