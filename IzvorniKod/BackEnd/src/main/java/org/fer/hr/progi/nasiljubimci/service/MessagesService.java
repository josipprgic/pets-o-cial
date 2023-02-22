package org.fer.hr.progi.nasiljubimci.service;

import lombok.RequiredArgsConstructor;
import org.fer.hr.progi.nasiljubimci.repository.MessagesRepository;
import org.fer.hr.progi.nasiljubimci.rest.model.UserDto;
import org.fer.hr.progi.nasiljubimci.tables.records.MessagesRecord;
import org.fer.hr.progi.nasiljubimci.tables.records.UserRecord;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MessagesService {

    private final MessagesRepository messagesRepository;

    public List<UserRecord> findMessagedPeople(Long id) {
        return messagesRepository.findMessagedPEople(id);
    }

    public List<MessagesRecord> findMessages(Long id, Long id1) {
        return messagesRepository.findmessages(id, id1);
    }

    public void sendMessage(Long id, Long id1, String content) {
        messagesRepository.sendMessage(id, id1, content);
    }
}
