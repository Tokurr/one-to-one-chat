package com.example.one_to_one_chat.dto;

import lombok.Data;

@Data
public class MessageSearchRequest {

    String receiverName;
    String messageText;
    int page;
    int size;


}
