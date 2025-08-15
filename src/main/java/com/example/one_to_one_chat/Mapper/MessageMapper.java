package com.example.one_to_one_chat.Mapper;

import com.example.one_to_one_chat.dto.MessageRequest;
import com.example.one_to_one_chat.model.Message;

public class MessageMapper {


    public Message messageRequestToMessage(MessageRequest messageRequest, String name)
    {
        Message message = new Message();
        message.setMessageText(messageRequest.getMessageText());
        message.setSenderName(name);
        message.setReceiverName(messageRequest.getReceiverName());

        return message;
    }

}
