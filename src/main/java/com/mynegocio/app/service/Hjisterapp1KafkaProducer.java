package com.mynegocio.app.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class Hjisterapp1KafkaProducer {

    private static final Logger log = LoggerFactory.getLogger(Hjisterapp1KafkaProducer.class);
    private static final String TOPIC = "topic_hjisterapp1";

    private KafkaTemplate<String, String> kafkaTemplate;

    public Hjisterapp1KafkaProducer(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendMessage(String message) {
        log.info("Producing message to {} : {}", TOPIC, message);
        this.kafkaTemplate.send(TOPIC, message);
    }
}
