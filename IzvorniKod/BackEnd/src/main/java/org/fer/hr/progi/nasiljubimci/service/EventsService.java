package org.fer.hr.progi.nasiljubimci.service;

import lombok.RequiredArgsConstructor;
import org.fer.hr.progi.nasiljubimci.enums.EventResponseType;
import org.fer.hr.progi.nasiljubimci.repository.EventsRepository;
import org.fer.hr.progi.nasiljubimci.repository.UserRepository;
import org.fer.hr.progi.nasiljubimci.rest.model.AddEventRequest;
import org.fer.hr.progi.nasiljubimci.rest.model.EventDto;
import org.fer.hr.progi.nasiljubimci.rest.model.EventResponse;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EventsService {

    private final EventsRepository eventsRepository;
    private final UserRepository userRepository;

    public void add(AddEventRequest addEventRequest, String username) {
        eventsRepository.add(addEventRequest, userRepository.findByUsername(username).getId());
    }

    public List<EventDto> get() {
        return eventsRepository.findAll().stream()
                .map(r -> EventDto.from(r, eventsRepository.getComing(r.getId()), userRepository.find(r.getOrganizer())))
                .collect(Collectors.toList());
    }

    public void maybeComing(EventResponse addEventRequest, String username) {
        eventsRepository.saveResponse(addEventRequest.getPostId(), userRepository.findByUsername(username).getId(), EventResponseType.MAYBE);
    }

    public void notComing(EventResponse addEventRequest, String username) {
        eventsRepository.saveResponse(addEventRequest.getPostId(), userRepository.findByUsername(username).getId(), EventResponseType.DENIED);
    }

    public void coming(EventResponse addEventRequest, String username) {
        eventsRepository.saveResponse(addEventRequest.getPostId(), userRepository.findByUsername(username).getId(), EventResponseType.ACCEPTED);
    }
}
