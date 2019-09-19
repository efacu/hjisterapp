package com.mynegocio.app.web.rest;

import com.mynegocio.app.service.Hjisterapp1KafkaProducer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api/hjisterapp-1-kafka")
public class Hjisterapp1KafkaResource {

    private final Logger log = LoggerFactory.getLogger(Hjisterapp1KafkaResource.class);

    private Hjisterapp1KafkaProducer kafkaProducer;

    public Hjisterapp1KafkaResource(Hjisterapp1KafkaProducer kafkaProducer) {
        this.kafkaProducer = kafkaProducer;
    }

    @PostMapping(value = "/publish")
    public void sendMessageToKafkaTopic(@RequestParam("message") String message) {
        log.debug("REST request to send to Kafka topic the message : {}", message);
        this.kafkaProducer.sendMessage(message);
    }
}
