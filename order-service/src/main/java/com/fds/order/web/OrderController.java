package com.fds.order.web;

import com.fds.order.domain.OrderEntity;
import com.fds.order.repo.OrderRepository;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/orders")
public class OrderController {
    private final OrderRepository orderRepository;
    public OrderController(OrderRepository orderRepository) { this.orderRepository = orderRepository; }

    @GetMapping
    public List<OrderEntity> list() { return orderRepository.findAll(); }

    @PostMapping
    public OrderEntity create(@Valid @RequestBody OrderEntity order) { return orderRepository.save(order); }

    @GetMapping("/{id}")
    public ResponseEntity<OrderEntity> get(@PathVariable Long id) {
        return orderRepository.findById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }
}

