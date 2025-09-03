package com.fds.notification.kafka;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class PaymentEventListener {
    private static final Logger log = LoggerFactory.getLogger(PaymentEventListener.class);

    @KafkaListener(topics = "payments", groupId = "notification-service")
    public void onPaymentEvent(ConsumerRecord<String, String> record) {
        log.info("Notification received: {}", record.value());
    }
}

