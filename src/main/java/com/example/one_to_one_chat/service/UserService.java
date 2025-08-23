package com.example.one_to_one_chat.service;

import com.example.one_to_one_chat.Mapper.Mapper;
import com.example.one_to_one_chat.dto.CreateUserRequest;
import com.example.one_to_one_chat.dto.UserDto;
import com.example.one_to_one_chat.exception.DuplicateUsernameException;
import com.example.one_to_one_chat.model.User;
import com.example.one_to_one_chat.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
public class UserService implements UserDetailsService {



    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    Mapper mapper = new Mapper();
    public UserService(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder) {

        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Optional<User> user = userRepository.findByUsername(username);
        return user.orElseThrow(EntityNotFoundException::new);
    }

    public Optional<UserDto> getByUserName(String username){
         UserDto  userDto = mapper.userToUserDto(userRepository.findByUsername(username).get());

        return Optional.ofNullable(userDto);
    }
    @CacheEvict(cacheNames = "users", key = "'allUsers'")

    public UserDto createUser(CreateUserRequest request)
    {

        if(userRepository.findByUsername(request.username()).isPresent())
        {
            throw new DuplicateUsernameException(request.username());

         }

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

        User savedUser = userRepository.save(user);

        UserDto dtoUser = mapper.userToUserDto(savedUser);

        return dtoUser;

    }
    @CacheEvict(cacheNames = "users", key = "'allUsers'")

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

    @Cacheable(cacheNames = "users", key = "'allUsers'")
    public List<UserDto> getAllUsers() {
        List<User> userList = userRepository.findAll(Sort.by("username"));

        return userList.stream()
                .map(mapper::userToUserDto)
                .toList();
    }

    public Page<UserDto> getUsers(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("username"));
        return userRepository.findAll(pageable)
                .map(mapper::userToUserDto); // User → UserDto
    }

    public Page<UserDto> searchUser(String username, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("username"));
        return userRepository.findByUsernameContainingIgnoreCase(username, pageable)
                .map(mapper::userToUserDto); // User → UserDto
    }



}
