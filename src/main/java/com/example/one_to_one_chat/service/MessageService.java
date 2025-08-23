package com.example.one_to_one_chat.service;

import com.example.one_to_one_chat.Mapper.Mapper;
import com.example.one_to_one_chat.dto.MessagePageableRequest;
import com.example.one_to_one_chat.dto.MessageRequest;
import com.example.one_to_one_chat.dto.MessageSearchRequest;
import com.example.one_to_one_chat.dto.UserDto;
import com.example.one_to_one_chat.exception.ReceiverNameNotFoundException;
import com.example.one_to_one_chat.model.Message;
import com.example.one_to_one_chat.model.User;
import com.example.one_to_one_chat.repository.MessageRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class MessageService {


    private final MessageRepository messageRepository;
    private final UserService userService;

    public MessageService(MessageRepository messageRepository, UserService userService) {
        this.messageRepository = messageRepository;
        this.userService = userService;
    }

    public void save(MessageRequest messageRequest,String name)
    {
        Mapper mapper = new Mapper();
        Message messsage = mapper.messageRequestToMessage(messageRequest,name);
        messageRepository.save(messsage);
    }


    public Page<Message>  getMessages(String sender, MessagePageableRequest messagePageableRequest)
    {

        Optional<UserDto> user = userService.getByUserName(messagePageableRequest.getReceiverName());


        if(user.isPresent())
        {

            Pageable pageable = PageRequest.of(messagePageableRequest.getPage(),messagePageableRequest.getSize(), Sort.by("localDateTime"));
            return  messageRepository.findBySenderNameAndReceiverNameOrSenderNameAndReceiverName(sender,messagePageableRequest.getReceiverName(),messagePageableRequest.getReceiverName(),sender,pageable);
        }

        throw new ReceiverNameNotFoundException("Receiver user not found");

    }

    public Page<Message> searchMessage(MessageSearchRequest messageSearchRequest,String senderName)
    {
        Pageable pageable = PageRequest.of(messageSearchRequest.getPage(),messageSearchRequest.getSize(), Sort.by("localDateTime"));
        return messageRepository.findBySenderNameAndReceiverNameAndMessageTextContainingIgnoreCaseOrSenderNameAndReceiverNameAndMessageTextContainingIgnoreCase(senderName,messageSearchRequest.getReceiverName(),messageSearchRequest.getMessageText(),messageSearchRequest.getReceiverName(),senderName,messageSearchRequest.getMessageText(),pageable);

    }

}
