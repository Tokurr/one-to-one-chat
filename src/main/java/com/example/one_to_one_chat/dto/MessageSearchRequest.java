package com.example.one_to_one_chat.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MessageSearchRequest {

    String receiverName;
    String messageText;
    int page;
    int size;


}
