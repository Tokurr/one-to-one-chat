package com.example.one_to_one_chat.model;

import org.springframework.security.core.GrantedAuthority;

public enum Role implements GrantedAuthority {

    ROLE_USER("ROLE_USER"),
    ROLE_ADMIN("ADMIN"),
    ROLE_MOD("MOD"),
    ROLE_FSK("FSK");

    private String value;

    Role(String value) {
        this.value = value;
    }

    @Override
    public String getAuthority() {
        return name();
    }
}
