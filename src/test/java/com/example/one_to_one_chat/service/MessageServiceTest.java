package com.example.one_to_one_chat.service;
import com.example.one_to_one_chat.dto.MessagePageableRequest;
import com.example.one_to_one_chat.dto.MessageRequest;
import com.example.one_to_one_chat.dto.MessageSearchRequest;
import com.example.one_to_one_chat.dto.UserDto;
import com.example.one_to_one_chat.exception.ReceiverNameNotFoundException;
import com.example.one_to_one_chat.model.Message;
import com.example.one_to_one_chat.model.User;
import com.example.one_to_one_chat.repository.MessageRepository;
import org.apache.catalina.UserDatabase;
import org.junit.jupiter.api.*;
import org.mockito.Mockito;
import org.springframework.data.domain.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.assertEquals;

class MessageServiceTest {

    private MessageService messageService;
    private MessageRepository messageRepository;
    private UserService userService;


    @BeforeEach
    void setUp(){
        messageRepository = Mockito.mock(MessageRepository.class);
        userService = Mockito.mock(UserService.class);
        messageService = new MessageService(messageRepository,userService);
    }


    @Test
    void shouldSaveMessageWhenRequestIsValid()
    {
        MessageRequest messageRequest = new MessageRequest("reciver","merhaba");
        String  name = "sender";

        messageService.save(messageRequest, name);
        Mockito.verify(messageRepository).save(Mockito.argThat(savedMessage ->
                savedMessage.getSenderName().equals("sender") &&
                        savedMessage.getReceiverName().equals("reciver") &&
                        savedMessage.getMessageText().equals("merhaba")
        ));

    }

    @Test
    void shouldReturnMessagesWhenReceiverExists(){

        String senderName = "kullanıcı1";
        MessagePageableRequest request = new MessagePageableRequest("kullanıcı2",0,3);

        UserDto receiverUser = new UserDto();
        receiverUser.setUsername("kullanıcı2");
        Message message = new Message(1,"kullanıcı2","kullanıcı1","Hello", LocalDateTime.now());
        List<Message> messageList = List.of(message);
        Page<Message> expectedPage = new PageImpl<>(messageList);
        Pageable pageable = PageRequest.of(0,3, Sort.by("localDateTime"));

        Mockito.when(userService.getByUserName("kullanıcı2")).thenReturn(Optional.of(receiverUser));
        Mockito.when(messageRepository.findBySenderNameAndReceiverNameOrSenderNameAndReceiverName(senderName,"kullanıcı2","kullanıcı2",senderName,pageable)).thenReturn(expectedPage);
        Page<Message> messages = messageService.getMessages(senderName,request);

        assertEquals(expectedPage,messages);

        Mockito.verify(messageRepository).findBySenderNameAndReceiverNameOrSenderNameAndReceiverName(senderName,"kullanıcı2","kullanıcı2",senderName,pageable);
        Mockito.verify(userService).getByUserName("kullanıcı2");

    }


    @Test
    void shouldThrowReturnMessagesWhenReceiverExists(){
        MessagePageableRequest request = new MessagePageableRequest("nonExistingUser", 0, 3);


        String receiverName = "okur4";
        Mockito.when(userService.getByUserName(receiverName)).thenReturn(Optional.empty());
        ReceiverNameNotFoundException exception = Assertions.assertThrows(
                ReceiverNameNotFoundException.class,
                () -> messageService.getMessages(receiverName, request)
        );
        Assertions.assertEquals("Receiver user not found", exception.getMessage());

        Mockito.verify(messageRepository, Mockito.never())
                .findBySenderNameAndReceiverNameOrSenderNameAndReceiverName(Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any());
    }

    @Test
    void shouldReturnMessagesWhenMessageTextMatchesBetweenTwoUsers()
    {
        String senderName = "kullanıcı1";
        MessageSearchRequest request = new MessageSearchRequest("kullanıcı2","merhaba",0,3);
        Message message = new Message(1,"kullanıcı2","kullanıcı1","merhaba", LocalDateTime.now());
        Pageable pageable = PageRequest.of(0,3, Sort.by("localDateTime"));

        List<Message> messageList = List.of(message);
        Page<Message> expectedPage = new PageImpl<>(messageList);
        Mockito.when(messageRepository.findBySenderNameAndReceiverNameAndMessageTextContainingIgnoreCaseOrSenderNameAndReceiverNameAndMessageTextContainingIgnoreCase(senderName,request.getReceiverName(),request.getMessageText(),request.getReceiverName(),senderName,request.getMessageText(),pageable)).thenReturn(expectedPage);
        Page<Message> messages = messageService.searchMessage(request,senderName);

        Assertions.assertEquals(expectedPage,messages);

        Mockito.verify(messageRepository).findBySenderNameAndReceiverNameAndMessageTextContainingIgnoreCaseOrSenderNameAndReceiverNameAndMessageTextContainingIgnoreCase(senderName,request.getReceiverName(),request.getMessageText(),request.getReceiverName(),senderName,request.getMessageText(),pageable);


    }



    @AfterEach
    void tearDown()
    {


    }


}