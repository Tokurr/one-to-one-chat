package com.example.one_to_one_chat.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SearchUserRequest {

    @Size(min = 3, max = 90 , message = "username must be 3-90 chars")
    @NotBlank
    String username;
    @Min(value = 0, message = "page must be at least 0")
    int page = 0;
    @Min(value = 2, message = "size must be at least 2")
    int size = 2;

}
