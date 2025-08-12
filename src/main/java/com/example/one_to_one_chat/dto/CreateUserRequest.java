package com.example.one_to_one_chat.dto;

import com.example.one_to_one_chat.model.Role;
import lombok.Builder;

import java.util.Set;

@Builder
public record CreateUserRequest(
        String name,
        String username,
        String password,
        Set<Role> authorities

) {
}
