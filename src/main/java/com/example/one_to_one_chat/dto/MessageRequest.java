package com.example.one_to_one_chat.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class MessageRequest {
    @NotBlank(message = "receiverName is required")
    @Size(min = 3, max = 90 , message = "receiverName must be 3-90 chars")
    String receiverName;
    @NotBlank(message = "messageText is required")
    String messageText;
}
