package com.example.one_to_one_chat.dto;


import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MessagePageableRequest {

    @NotBlank(message = "receiverName is required")
    String receiverName;
    @Min(value = 0, message = "page must be at least 0")
    int page = 0;
    @Min(value = 2, message = "size must be at least 2")
    int size = 2;

}
