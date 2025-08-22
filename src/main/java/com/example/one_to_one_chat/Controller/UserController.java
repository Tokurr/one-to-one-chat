package com.example.one_to_one_chat.Controller;


import com.example.one_to_one_chat.dto.AuthRequest;
import com.example.one_to_one_chat.dto.CreateUserRequest;
import com.example.one_to_one_chat.dto.SearchUserRequest;
import com.example.one_to_one_chat.model.User;
import com.example.one_to_one_chat.service.AuthenticationService;
import com.example.one_to_one_chat.service.JwtService;
import com.example.one_to_one_chat.service.UserService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/auth")
public class UserController {

    private final UserService service;

    private final JwtService jwtService;

    private final AuthenticationService authenticationService;


    public UserController(UserService service, JwtService jwtService, AuthenticationService authenticationService) {
        this.service = service;
        this.jwtService = jwtService;
        this.authenticationService = authenticationService;
    }


    @PostMapping("/generateToken")
    public String generateToken(@Valid @RequestBody AuthRequest request)
    {
        return authenticationService.authentication(request);
    }

    @PostMapping("/create")
    public User addUser( @Valid @RequestBody CreateUserRequest user)
    {
        return service.createUser(user);
    }


    @DeleteMapping("/delete")
    public ResponseEntity<String> delete(Authentication authentication)
    {
        service.deleteUser(authentication.getName());
        return ResponseEntity.ok("Your account has been deleted successfully.");

    }

    @GetMapping("/getUsers")
    public ResponseEntity<Page<User>> getUsers( @RequestParam(defaultValue = "0") @Min(0) int page, @RequestParam(defaultValue = "10") @Min(1)  int size)
    {
      return ResponseEntity.ok(service.getUsers(page, size));
    }

    @GetMapping("/all")
    public ResponseEntity<List<User>> getAllUsers() {
        return ResponseEntity.ok(service.getAllUsers());
    }

    @GetMapping("/searchUsers")
    public ResponseEntity<Page<User>> searchUsers(@Valid @ModelAttribute SearchUserRequest request){
        return ResponseEntity.ok(service.searchUser(request.getUsername(),request.getPage(),request.getSize()));
    }


}
