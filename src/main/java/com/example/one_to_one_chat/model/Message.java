package com.example.one_to_one_chat.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "message")
public class Message {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    Integer id;
    String receiverName;
    String senderName;
    String messageText;


}
