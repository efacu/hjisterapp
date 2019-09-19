package com.mynegocio.app.web.rest;

import com.mynegocio.app.Hjisterapp1App;
import com.mynegocio.app.service.Hjisterapp1KafkaProducer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@EmbeddedKafka
@SpringBootTest(classes = Hjisterapp1App.class)
public class Hjisterapp1KafkaResourceIT {

    @Autowired
    private Hjisterapp1KafkaProducer kafkaProducer;

    private MockMvc restMockMvc;

    @BeforeEach
    public void setup() {
        Hjisterapp1KafkaResource kafkaResource = new Hjisterapp1KafkaResource(kafkaProducer);

        this.restMockMvc = MockMvcBuilders.standaloneSetup(kafkaResource)
            .build();
    }

    @Test
    public void sendMessageToKafkaTopic() throws Exception {
        restMockMvc.perform(post("/api/hjisterapp-1-kafka/publish?message=yolo"))
            .andExpect(status().isOk());
    }
}
