package com.fds.delivery.web;

import com.fds.delivery.domain.Delivery;
import com.fds.delivery.repo.DeliveryRepository;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/deliveries")
public class DeliveryController {
    private final DeliveryRepository deliveryRepository;
    public DeliveryController(DeliveryRepository deliveryRepository) { this.deliveryRepository = deliveryRepository; }

    @GetMapping
    public List<Delivery> list() { return deliveryRepository.findAll(); }

    @PostMapping
    public Delivery create(@Valid @RequestBody Delivery d) { return deliveryRepository.save(d); }

    @GetMapping("/{id}")
    public ResponseEntity<Delivery> get(@PathVariable Long id) {
        return deliveryRepository.findById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }
}

