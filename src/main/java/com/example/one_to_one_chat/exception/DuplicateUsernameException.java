package com.example.one_to_one_chat.exception;

public class DuplicateUsernameException extends RuntimeException {
    public DuplicateUsernameException(String username) {
        super("Username already taken: " + username);
    }
}