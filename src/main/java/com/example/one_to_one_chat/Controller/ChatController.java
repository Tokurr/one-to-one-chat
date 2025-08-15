package com.example.one_to_one_chat.Controller;

import com.example.one_to_one_chat.dto.MessagePageableRequest;
import com.example.one_to_one_chat.dto.MessageRequest;
import com.example.one_to_one_chat.dto.MessageResponse;
import com.example.one_to_one_chat.dto.MessageSearchRequest;
import com.example.one_to_one_chat.model.Message;
import com.example.one_to_one_chat.service.MessageService;
import jakarta.validation.Valid;
import org.apache.coyote.Response;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping(path = "/message")
public class ChatController {
    private final SimpMessagingTemplate template;
    private final MessageService messageService;

    public ChatController(SimpMessagingTemplate template, MessageService messageService) {
        this.template = template;

        this.messageService = messageService;
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
        messageService.save(payload,principal.getName());

    }


    @GetMapping("/getPageable")
    public ResponseEntity< Page<Message> >getPageable(
            @Valid @ModelAttribute MessagePageableRequest request,
            Principal principal) {

        return ResponseEntity.ok(messageService.getMessages(principal.getName(),request)) ;
    }

    @GetMapping("/searchMessage")
    public ResponseEntity<Page<Message>> searchMessage(@Valid @ModelAttribute MessageSearchRequest request, Principal principal)
    {
       return   ResponseEntity.ok(messageService.searchMessage(request,principal.getName()));

    }


}
