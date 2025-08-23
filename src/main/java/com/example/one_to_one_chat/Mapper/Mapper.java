package com.example.one_to_one_chat.Mapper;

import com.example.one_to_one_chat.dto.MessageRequest;
import com.example.one_to_one_chat.dto.UserDto;
import com.example.one_to_one_chat.model.Message;
import com.example.one_to_one_chat.model.User;

public class Mapper {


    public Message messageRequestToMessage(MessageRequest messageRequest, String name)
    {
        Message message = new Message();
        message.setMessageText(messageRequest.getMessageText());
        message.setSenderName(name);
        message.setReceiverName(messageRequest.getReceiverName());

        return message;
    }

    public UserDto userToUserDto(User user)
    {
        UserDto userDto = new UserDto();
        userDto.setName(user.getName());
        userDto.setUsername(user.getUsername());
        userDto.setAuthorities(user.getAuthorities());
        return userDto;
    }

}
