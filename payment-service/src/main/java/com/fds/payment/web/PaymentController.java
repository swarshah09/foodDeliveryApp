package com.fds.payment.web;

import com.fds.payment.domain.Payment;
import com.fds.payment.domain.PaymentStatus;
import com.fds.payment.repo.PaymentRepository;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/payments")
public class PaymentController {
    private final PaymentRepository paymentRepository;
    private final KafkaTemplate<String, String> kafkaTemplate;

    public PaymentController(PaymentRepository paymentRepository, KafkaTemplate<String, String> kafkaTemplate) {
        this.paymentRepository = paymentRepository;
        this.kafkaTemplate = kafkaTemplate;
    }

    @GetMapping
    public List<Payment> list() { return paymentRepository.findAll(); }

    @PostMapping
    public Payment create(@Valid @RequestBody Payment p) {
        p.setStatus(PaymentStatus.SUCCESS);
        Payment saved = paymentRepository.save(p);
        kafkaTemplate.send("payments", "PAYMENT_SUCCESS:" + saved.getOrderId());
        return saved;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Payment> get(@PathVariable Long id) {
        return paymentRepository.findById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }
}

