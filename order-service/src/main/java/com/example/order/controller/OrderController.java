package com.example.order.controller;

import com.example.order.messaging.OrderEvent;
import com.example.order.messaging.OrderProducer;
import com.example.order.model.CustomerOrder;
import com.example.order.repository.CustomerOrderRepository;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.time.Instant;
import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController {
    private final CustomerOrderRepository customerOrderRepository;
    private final OrderProducer orderProducer;

    public OrderController(CustomerOrderRepository customerOrderRepository, OrderProducer orderProducer) {
        this.customerOrderRepository = customerOrderRepository;
        this.orderProducer = orderProducer;
    }

    @GetMapping
    public List<CustomerOrder> list() {
        return customerOrderRepository.findAll();
    }

    @PostMapping
    public ResponseEntity<CustomerOrder> create(@Valid @RequestBody CustomerOrder order) {
        order.setCreatedAt(Instant.now());
        CustomerOrder saved = customerOrderRepository.save(order);
        orderProducer.sendOrderCreated(OrderEvent.builder()
                .orderId(saved.getId())
                .productName(saved.getProductName())
                .quantity(saved.getQuantity())
                .build());
        return ResponseEntity.created(URI.create("/api/orders/" + saved.getId())).body(saved);
    }
}

