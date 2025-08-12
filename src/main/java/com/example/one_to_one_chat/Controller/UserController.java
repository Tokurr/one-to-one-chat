package com.example.one_to_one_chat.Controller;


import com.example.one_to_one_chat.dto.AuthRequest;
import com.example.one_to_one_chat.dto.CreateUserRequest;
import com.example.one_to_one_chat.model.User;
import com.example.one_to_one_chat.service.JwtService;
import com.example.one_to_one_chat.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/auth")
public class UserController {

    private final UserService service;

    private final JwtService jwtService;

    private final AuthenticationManager authenticationManager;

    public UserController(UserService service, JwtService jwtService, AuthenticationManager authenticationManager) {
        this.service = service;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
    }


    @PostMapping("/generateToken")
    public String generateToken(@RequestBody AuthRequest request)
    {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.username(),request.password()));
        if(authentication.isAuthenticated())
        {
            return jwtService.generateToken(request.username());
        }

        throw new UsernameNotFoundException("invalid username {} " + request.username());
    }

    @PostMapping("/addNewUser")
    public User addUser(@RequestBody CreateUserRequest user)
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
