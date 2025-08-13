package com.example.one_to_one_chat.dto;

import com.example.one_to_one_chat.model.Role;
import lombok.Builder;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.Set;

@Builder
public record CreateUserRequest(


        @NotBlank(message = "name is required")
        @Size(min = 3, max = 90, message = "name must be 3-90 chars")
        String name,

        @NotBlank(message = "username is required")
        @Size(min = 3, max = 90 , message = "username must be 3-90 chars")
        String username,

        @NotBlank(message = "password is required")
        String password,

        @NotNull(message = "authorities is required")
        Set<Role> authorities

) {
}
