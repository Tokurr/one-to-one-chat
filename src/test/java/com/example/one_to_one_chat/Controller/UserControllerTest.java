package com.example.one_to_one_chat.Controller;


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
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();

    }

    @Test
    void whenCreateUserCalled_itShouldSaveUser() throws Exception{

        String requestJson = """
                {
                    "name" : "tayyib",
                    "username" : "okur4",
                    "password" : "1234",
                    "authorities": ["ROLE_USER"]
               
                }
                
                """;
        mockMvc.perform(post("/auth/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("tayyib"))
                .andExpect(jsonPath("$.username").value("okur4"));
    }


    @Test
    @WithMockUser(username = "kullanıcı", roles = "USER")
    void whenDeleteCalled_itShouldDeleteUser() throws Exception {
        User user = User.builder()
                .name("kullanıcı")
                .username("kullanıcı")
                .password("1234")
                .authorities(Set.of(Role.ROLE_USER))
                .accountNonExpired(true
                ).isEnabled(true).accountNonLocked(true).credentialsNonExpired(true).build();
        userRepository.save(user);

        mockMvc.perform(delete("/auth/delete"))
                .andExpect(status().isOk())
                .andExpect(content().string("Your account has been deleted successfully."));

        Assertions.assertTrue(userRepository.findByUsername("kullanıcı").isEmpty());

    }


    @Test
    @WithMockUser(username = "admin", roles = "USER")
    void whenGetUsersCalled_itShouldReturnPagedUsers() throws Exception{

        mockMvc.perform(get("/auth/getUsers")
                        .param("page", "0")
                        .param("size", "2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size").value(2))
                .andExpect(jsonPath("$.number").value(0))
        ;


    }

    @Test
    @WithMockUser(username = "admin", roles = "USER")
    void whenGetSearchUser_itShouldReturnSearchedUser() throws Exception {

        User user = User.builder()
                .name("Test Kullanıcı")
                .username("kullanıcı")
                .password("1234")
                .authorities(Set.of(Role.ROLE_USER))
                .accountNonExpired(true)
                .accountNonLocked(true)
                .credentialsNonExpired(true)
                .isEnabled(true)
                .build();
        userRepository.save(user);

        SearchUserRequest request = new SearchUserRequest("kullanıcı",0,2);
        mockMvc.perform(get("/auth/searchUsers") .param("username","kullanıcı").param("page", "0").param("size","2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].username").value("kullanıcı"))
                .andExpect(jsonPath("$.size").value(2))
                .andExpect(jsonPath("$.number").value(0));;

    }



    @AfterEach
    void tearDown() {
    }
}