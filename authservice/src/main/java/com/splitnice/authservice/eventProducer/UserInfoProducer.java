package com.splitnice.authservice.eventProducer;

import com.splitnice.authservice.entities.UserEvent;
import com.splitnice.authservice.model.UserInfoDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

@Service
public class UserInfoProducer {
    private final KafkaTemplate<String, UserInfoEvent> kafkaTemplate;
   @Value("${app.kafka.topic}")
   private String TOPIC_NAME;

    @Autowired
    UserInfoProducer(KafkaTemplate<String, UserInfoEvent> kafkatemplate) {
        this.kafkaTemplate=kafkatemplate;
    }

    public void sendMessage(UserInfoEvent userInfoDto) {
        Message<UserInfoEvent> message = MessageBuilder.withPayload(userInfoDto).setHeader(KafkaHeaders.TOPIC, TOPIC_NAME).build();
        kafkaTemplate.send(message);
    }


}
