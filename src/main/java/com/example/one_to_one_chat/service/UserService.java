package com.example.one_to_one_chat.service;

import com.example.one_to_one_chat.dto.CreateUserRequest;
import com.example.one_to_one_chat.model.User;
import com.example.one_to_one_chat.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
public class UserService implements UserDetailsService {


    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Optional<User> user = userRepository.findByUsername(username);

        return user.orElseThrow(EntityNotFoundException::new);
    }

    public Optional<User> getByUserName(String username){
        return userRepository.findByUsername(username);
    }

    public User createUser(CreateUserRequest request)
    {
        User user = User.builder()
                .name(request.name())
                .username(request.username())
                .password(passwordEncoder.encode(request.password()))
                .authorities(request.authorities())
                .accountNonExpired(true)
                .credentialsNonExpired(true)
                .isEnabled(true)
                .accountNonLocked(true)
                .build();
        return userRepository.save(user);

    }

    public void deleteUser(String username)
    {
        Optional<User> user = userRepository.findByUsername(username);

        if(user.isPresent())
        {
            userRepository.delete(user.get());

        }
        else
        {
            throw new UsernameNotFoundException("user not found");
        }
    }



}
