package org.fer.hr.progi.nasiljubimci.rest.controller;

import jdk.jfr.Event;
import lombok.RequiredArgsConstructor;
import org.fer.hr.progi.nasiljubimci.rest.model.*;
import org.fer.hr.progi.nasiljubimci.service.EventsService;
import org.fer.hr.progi.nasiljubimci.service.MessagesService;
import org.fer.hr.progi.nasiljubimci.service.PetsService;
import org.fer.hr.progi.nasiljubimci.service.UserService;
import org.fer.hr.progi.nasiljubimci.tables.records.UserRecord;
import org.fer.hr.progi.nasiljubimci.web.security.AuthenticationService;
import org.springframework.http.MediaType;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class EventController {

    private final AuthenticationService authenticationService;
    private final UserService userService;
    private final PetsService petsService;
    private final MessagesService messagesService;
    private final EventsService eventsService;

    @PostMapping("/events/add")
    public void add(@RequestBody AddEventRequest addEventRequest) {
        eventsService.add(addEventRequest, ((UserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername());
    }

    @PostMapping("/events/coming")
    public void coming(@RequestBody EventResponse addEventRequest) {
        eventsService.coming(addEventRequest, ((UserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername());
    }

    @PostMapping("/events/notComing")
    public void notComing(@RequestBody EventResponse addEventRequest) {
        eventsService.notComing(addEventRequest, ((UserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername());
    }

    @PostMapping("/events/maybe")
    public void maybe(@RequestBody EventResponse addEventRequest) {
        eventsService.maybeComing(addEventRequest, ((UserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername());
    }

    @GetMapping("/events")
    public List<EventDto> get() {
        String user = ((UserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();

        return eventsService.get().stream()
                .filter(e -> !userService.getBlockedStatus(e.getOrganizer(), user).isBlocked())
                .collect(Collectors.toList());
    }
}
