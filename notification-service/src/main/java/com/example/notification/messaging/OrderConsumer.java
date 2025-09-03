package com.example.notification.messaging;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class OrderConsumer {
    private static final Logger log = LoggerFactory.getLogger(OrderConsumer.class);

    @KafkaListener(topics = "orders", groupId = "notification-service", containerFactory = "kafkaListenerContainerFactory")
    public void onOrderCreated(OrderEvent event) {
        log.info("Received order event: {}", event);
        // Here you could send an email, SMS, push, etc.
    }
}

