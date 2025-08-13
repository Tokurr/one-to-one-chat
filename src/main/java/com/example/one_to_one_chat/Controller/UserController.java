package com.example.one_to_one_chat.Controller;


import com.example.one_to_one_chat.dto.AuthRequest;
import com.example.one_to_one_chat.dto.CreateUserRequest;
import com.example.one_to_one_chat.model.User;
import com.example.one_to_one_chat.service.AuthenticationService;
import com.example.one_to_one_chat.service.JwtService;
import com.example.one_to_one_chat.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;


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


}
