package com.assembly.vote.assembly.domain.application.publisher;

import com.assembly.vote.assembly.domain.core.model.dto.UserNotificationDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
@Log
public class NotificationSender {
 
    private final RabbitTemplate rabbitTemplate;
 
    private final Queue queue;


    public void send(UserNotificationDTO notificationDTO) {
        try {
            log.info("Enviando notificação de novo registro...");
            rabbitTemplate.convertAndSend(this.queue.getName(), notificationDTO);
            log.info("notificação enviada...");

        }catch (Exception e){
            e.printStackTrace();
            log.severe("Erro ao enviar notificação: "+e.getMessage());
        }
    }




}