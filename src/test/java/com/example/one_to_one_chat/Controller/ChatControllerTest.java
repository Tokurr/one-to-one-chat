package com.example.one_to_one_chat.Controller;

import com.example.one_to_one_chat.dto.MessagePageableRequest;
import com.example.one_to_one_chat.service.MessageService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.*;
import com.example.one_to_one_chat.dto.SearchUserRequest;
import com.example.one_to_one_chat.model.Role;
import com.example.one_to_one_chat.model.User;
import com.example.one_to_one_chat.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Set;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class ChatControllerTest {


    @Autowired
    private MockMvc mockMvc;




    @BeforeEach
    void setUp() {
    }


    @Test
    @WithMockUser(username = "okur4",roles = "USER")
    void whengetPageableCalled_itshouldReturnMessages() throws Exception {
        mockMvc.perform(get("/message/getPageable").param("receiverName","okurrr")
                .param("page","0")
                .param("size","2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].senderName").value("okur4"))
                .andExpect(jsonPath("$.content[0].receiverName").value("okurrr"))
                .andExpect(jsonPath("$.size").value(2))
                .andExpect(jsonPath("$.number").value(0));

    }


    @Test
    @WithMockUser(username = "okur4",roles = "USER")
    void whenSearchMessageCalled_itShouldReturnSearchedMessages() throws Exception
    {
        mockMvc.perform(get("/message/searchMessage").param("receiverName","okurrr")
                .param("messageText","merhaba").param("page", "0")
                .param("size","2"))
                .andExpect(jsonPath("$.content").isArray()).
                 andExpect(jsonPath("$.content[0].senderName").value("okur4"))
                .andExpect(jsonPath("$.content[0].receiverName").value("okurrr"))
                .andExpect(jsonPath("$.content[0].messageText").value("merhaba"));

    }

    @AfterEach
    void tearDown() {
    }
}