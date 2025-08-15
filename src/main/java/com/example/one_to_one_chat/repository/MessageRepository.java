package com.example.one_to_one_chat.repository;

import com.example.one_to_one_chat.model.Message;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MessageRepository extends JpaRepository<Message,Integer> {

    Page<Message> findBySenderNameAndReceiverNameOrSenderNameAndReceiverName(

         String sender, String receiver,
         String receiver2, String sender2,
         Pageable pageable

    );

    Page<Message> findBySenderNameAndReceiverNameAndMessageTextContainingIgnoreCaseOrSenderNameAndReceiverNameAndMessageTextContainingIgnoreCase(
            String sender1, String receiver1, String keyword1,
            String sender2, String receiver2, String keyword2,
            Pageable pageable
    );

}
