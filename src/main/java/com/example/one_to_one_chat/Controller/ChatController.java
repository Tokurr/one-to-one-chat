package com.example.one_to_one_chat.Controller;

import com.example.one_to_one_chat.dto.MessageRequest;
import com.example.one_to_one_chat.dto.MessageResponse;
import com.example.one_to_one_chat.model.Message;
import jakarta.validation.Valid;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.security.Principal;

@Controller
public class ChatController {
    private final SimpMessagingTemplate template;


    public ChatController(SimpMessagingTemplate template) {
        this.template = template;

    }
    @MessageMapping("/direct.send")
    public void sendDirect(@Valid MessageRequest payload, Principal principal) {
        if (principal == null) {
            return;
        }

        String receiverName = payload.getReceiverName();
        MessageResponse messageResponse = new MessageResponse();
        messageResponse.setMessageText(payload.getMessageText());
        messageResponse.setSenderName(principal.getName());
        template.convertAndSendToUser(receiverName,"/queue/messages",messageResponse);
    }

}
