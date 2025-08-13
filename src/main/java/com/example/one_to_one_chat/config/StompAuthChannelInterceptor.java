package com.example.one_to_one_chat.config;

import com.example.one_to_one_chat.service.JwtService;
import com.example.one_to_one_chat.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessagingException;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Component;

@Component
public class StompAuthChannelInterceptor implements ChannelInterceptor {
    private final JwtService jwtService;
    private final UserService userService;
    private static final Logger log = LoggerFactory.getLogger(StompAuthChannelInterceptor.class);

    public StompAuthChannelInterceptor(JwtService jwtService, UserService userService) {
        this.jwtService = jwtService;
        this.userService = userService;
    }
    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {

        StompHeaderAccessor acc = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
        if (acc == null) return message;

        if (StompCommand.CONNECT.equals(acc.getCommand())) {
            String auth = acc.getFirstNativeHeader("Authorization");

            if (auth == null) throw new MessagingException("AUTH_UNAUTHORIZED: missing bearer token");


            String token = null;
            if(auth.startsWith("Bearer"))
            {
                token = auth.substring(7).trim();
            }

            if (token.isBlank()) throw new MessagingException("AUTH_UNAUTHORIZED: empty token");

            try {
                String username = jwtService.extractUser(token);
                var userDetails = userService.loadUserByUsername(username);
                if (!jwtService.validateToken(token, userDetails)) {
                    throw new MessagingException("AUTH_UNAUTHORIZED: invalid token");
                }

                var principal = new UsernamePasswordAuthenticationToken(
                        userDetails.getUsername(), null, userDetails.getAuthorities());

                acc.setUser(principal);
                log.info("WS CONNECT authenticated as '{}'", userDetails.getUsername());

                return MessageBuilder.createMessage(message.getPayload(), acc.getMessageHeaders());

            } catch (Exception e) {
                throw new MessagingException("AUTH_UNAUTHORIZED: " + e.getMessage(), e);
            }
        }

        return message;
    }


}
