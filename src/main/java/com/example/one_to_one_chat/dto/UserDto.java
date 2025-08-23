package com.example.one_to_one_chat.dto;

import com.example.one_to_one_chat.model.Role;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {

    private String name;
    @Column(unique = true, nullable = false)
    @Size(min = 3, max = 90 , message = "username must be 3-90 chars")
    private String username;

    private Set<Role> authorities;
}
