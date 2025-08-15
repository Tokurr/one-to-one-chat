package com.example.one_to_one_chat.exception;

public class ReceiverNameNotFoundException extends RuntimeException{

    public ReceiverNameNotFoundException (String msg)
    {
        super(msg);
    }

}
