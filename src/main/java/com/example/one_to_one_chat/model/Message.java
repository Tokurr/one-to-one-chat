package com.example.one_to_one_chat.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "message")
@AllArgsConstructor
@NoArgsConstructor
public class Message {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    Integer id;
    String receiverName;
    String senderName;
    String messageText;
    LocalDateTime localDateTime;


}
