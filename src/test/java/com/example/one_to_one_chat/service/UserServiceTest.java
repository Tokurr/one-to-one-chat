package com.example.one_to_one_chat.service;

import com.example.one_to_one_chat.dto.CreateUserRequest;
import com.example.one_to_one_chat.dto.UserDto;
import com.example.one_to_one_chat.exception.DuplicateUsernameException;
import com.example.one_to_one_chat.model.Role;
import com.example.one_to_one_chat.model.User;
import com.example.one_to_one_chat.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.data.domain.*;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.List;
import java.util.Optional;
import java.util.Set;

class UserServiceTest {

    private UserRepository userRepository;
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    private UserService userService;

    @BeforeEach
    void setUp() {
        userRepository = Mockito.mock(UserRepository.class);
        bCryptPasswordEncoder = Mockito.mock(BCryptPasswordEncoder.class);
        userService = new UserService(userRepository,bCryptPasswordEncoder);
    }

    @Test
    void whenLoadUserByUsernameCalledAndUserExists_itShouldReturnUserDetails(){
        String username = "kullanıcı";
        String password = "password";

        User user = new User();
        user.setUsername(username);
        user.setPassword(password);

        Mockito.when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));
        UserDetails userDetails = userService.loadUserByUsername(username);

        Assertions.assertEquals(userDetails.getUsername(),username);
        Assertions.assertEquals(userDetails.getPassword(),password);

        Mockito.verify(userRepository).findByUsername(username);

    }


    @Test
    void whenGetByUserNameCalledAndUserExists_itShouldReturnUser()
    {
        String username = "username";

        User user = returnUser();

        Mockito.when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));
        Optional<UserDto> expectedUser = userService.getByUserName(username);
        Assertions.assertEquals(expectedUser, Optional.of(user));

        Mockito.verify(userRepository).findByUsername(username);

    }

    @Test
    void whenLoadUserByUsernameCalledAndUserDoesNotExist_itShouldThrowException()
    {
        Mockito.when(userRepository.findByUsername("unknown")).thenReturn(Optional.empty());
        Assertions.assertThrows(EntityNotFoundException.class,
                () -> userService.loadUserByUsername("unknown"));
    }

    @Test
    void whenCreateUserCalledAndUsernameNotTaken_itShouldCreateUser()
    {

        CreateUserRequest request = CreateUserRequest.builder()
                        .username("username")
                        .name("name")
                        .password("password")
                        .authorities(Set.of(Role.ROLE_USER))
                        .build();

        Mockito.when(userRepository.findByUsername("username")).thenReturn(Optional.empty());
        Mockito.when(bCryptPasswordEncoder.encode("password")).thenReturn("encodedpassword");

        User savedUser = returnUser();
        Mockito.when(userRepository.save(Mockito.any(User.class))).thenReturn(savedUser);

        UserDto user = userService.createUser(request);

        Assertions.assertEquals(user,savedUser);

        Mockito.verify(userRepository).findByUsername("username");
        Mockito.verify(bCryptPasswordEncoder).encode("password");
        Mockito.verify(userRepository).save(Mockito.any(User.class));


    }

    @Test
    void whenCreateUserCalledAndUsernameAlreadyExists_itShouldThrowException()
    {

        CreateUserRequest request = CreateUserRequest.builder()
                .username("username")
                .name("name")
                .password("password")
                .authorities(Set.of(Role.ROLE_USER))
                .build();

        Mockito.when(userRepository.findByUsername(request.username())).thenReturn(Optional.of(new User()));

        Assertions.assertThrows(DuplicateUsernameException.class,()-> userService.createUser(request));
        Mockito.verify(userRepository, Mockito.never()).save(Mockito.any(User.class));



    }

    @Test
    void whenDeleteUserCalledAndUserExists_itShouldDeleteUser()
    {
        String username = "username";

        User user = returnUser();

        Mockito.when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));
        userService.deleteUser(username);

        Mockito.verify(userRepository).findByUsername(username);
        Mockito.verify(userRepository).delete(user);

    }

    @Test
    void whenDeleteUserCalledAndUserDoesNotExist_itShouldThrowException()
    {
        String username = "username";

        Mockito.when(userRepository.findByUsername(username)).thenReturn(Optional.empty());

        UsernameNotFoundException exception = Assertions.assertThrows(UsernameNotFoundException.class,() -> userService.deleteUser(username));
        Assertions.assertEquals("user not found", exception.getMessage());

        Mockito.verify(userRepository).findByUsername(username);
        Mockito.verify(userRepository,Mockito.never()).delete(Mockito.any(User.class));

    }

    @Test
    void whenGetUsersCalledWithValidPageAndSize_itShouldReturnPagedUsers()
    {
        int page = 0;
        int size = 3;
        User user = returnUser();
        List<User> userList = List.of(user);
        Page<User> expectedUsers = new PageImpl<>(userList);

        Pageable pageable = PageRequest.of(page,size,Sort.by("username"));

        Mockito.when(userRepository.findAll(pageable)).thenReturn(expectedUsers);

        userService.getUsers(page,size);
        Mockito.verify(userRepository).findAll(pageable);

    }


    @Test
    void whenSearchUserCalledWithUsernamePageAndSize_itShouldReturnSearchedUsers()
    {
        String username = "username";
        int page = 0;
        int size = 3;
        User user = returnUser();
        List<User> userList = List.of(user);
        Page<User> expectedUsers = new PageImpl<>(userList);

        Pageable pageable = PageRequest.of(page,size,Sort.by("username"));



        Mockito.when(userRepository.findByUsernameContainingIgnoreCase(username,pageable)).thenReturn(expectedUsers);
        Page<UserDto> result =  userService.searchUser(username,page,size);
        Assertions.assertEquals(expectedUsers,result);
        Mockito.verify(userRepository).findByUsernameContainingIgnoreCase(username,pageable);

    }

    public User returnUser()
    {
        User user = User.builder()
                .name("name")
                .username("username")
                .password("encodedpassword")
                .authorities(Set.of(Role.ROLE_USER))
                .accountNonExpired(true)
                .credentialsNonExpired(true)
                .isEnabled(true)
                .accountNonLocked(true)
                .build();
        return user;
    }

    @AfterEach
    void tearDown() {

    }


}