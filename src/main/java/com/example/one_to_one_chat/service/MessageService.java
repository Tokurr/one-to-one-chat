package com.example.one_to_one_chat.service;

import com.example.one_to_one_chat.Mapper.MessageMapper;
import com.example.one_to_one_chat.dto.MessageRequest;
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
        MessageMapper mapper = new MessageMapper();
        Message messsage = mapper.messageRequestToMessage(messageRequest,name);
        messageRepository.save(messsage);
    }

    public Page<Message> getMessages( String sender, String receiver, int page,int size)
    {

        Optional<User> user = userService.getByUserName(receiver);


        if(user.isPresent())
        {

            Pageable pageable = PageRequest.of(page,size, Sort.by("localDateTime"));
            return messageRepository.findBySenderNameAndReceiverNameOrSenderNameAndReceiverName(sender,receiver,receiver,sender,pageable);
        }

        throw new ReceiverNameNotFoundException("Receiver user not found");

    }



}
