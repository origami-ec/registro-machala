package org.origami.zull.conf;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class KafkaLogServices {

    private static final Logger logger = LoggerFactory.getLogger(KafkaLogServices.class);
    @Value("${spring.kafka.topics}")
    private String[] topics;

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @Async
    public void sendMessageLogFile(String message) {
        logger.info(String.format("#### -> Mensaje -> %s", message));
        this.kafkaTemplate.send(topics[0], message);
    }

    @Async
    public void sendMessageLogAsgard(String message) {
        logger.info(String.format("#### -> Mensaje -> %s", message));
        this.kafkaTemplate.send(topics[1], message);
    }

}
