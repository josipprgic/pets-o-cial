package org.fer.hr.progi.nasiljubimci.repository;

import lombok.RequiredArgsConstructor;
import org.fer.hr.progi.nasiljubimci.enums.EventResponseType;
import org.fer.hr.progi.nasiljubimci.enums.EventVisibility;
import org.fer.hr.progi.nasiljubimci.rest.model.AddEventRequest;
import org.fer.hr.progi.nasiljubimci.rest.model.EventDto;
import org.fer.hr.progi.nasiljubimci.rest.model.EventResponse;
import org.fer.hr.progi.nasiljubimci.tables.EventResponses;
import org.fer.hr.progi.nasiljubimci.tables.Events;
import org.fer.hr.progi.nasiljubimci.tables.User;
import org.fer.hr.progi.nasiljubimci.tables.records.EventResponsesRecord;
import org.fer.hr.progi.nasiljubimci.tables.records.EventsRecord;
import org.fer.hr.progi.nasiljubimci.tables.records.UserRecord;
import org.jooq.DSLContext;
import org.jooq.types.YearToSecond;
import org.springframework.stereotype.Repository;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class EventsRepository {

    private final DSLContext dslContext;

    public void add(AddEventRequest addEventRequest, Long id) {
        dslContext.insertInto(Events.EVENTS)
                .set(new EventsRecord()
                        .setDescription(addEventRequest.getDescription())
                        .setDuration(YearToSecond.valueOf(Duration.ofHours(addEventRequest.getDuration())))
                        .setLocation(addEventRequest.getLocation())
                        .setOrganizer(id)
                        .setStartDate(LocalDateTime.of(addEventRequest.getStartDate(), addEventRequest.getStartTime()))
                        .setVisibility(EventVisibility.ALL))
                .execute();
    }

    public List<EventsRecord> findAll() {
        return dslContext.selectFrom(Events.EVENTS)
                .fetchInto(Events.EVENTS);
    }

    public List<String> getComing(Long id) {
        return dslContext.selectFrom(Events.EVENTS.join(EventResponses.EVENT_RESPONSES).on(Events.EVENTS.ID.eq(EventResponses.EVENT_RESPONSES.EVENT_ID)).join(User.USER).on(EventResponses.EVENT_RESPONSES.USER_ID.eq(User.USER.ID)))
                .where(EventResponses.EVENT_RESPONSES.RESPONSE.eq(EventResponseType.ACCEPTED).and(Events.EVENTS.ID.eq(id)))
                .fetchInto(User.USER).stream().map(UserRecord::getUsername).collect(Collectors.toList());
    }

    public void saveResponse(Long postId, Long id, EventResponseType maybe) {
        dslContext.deleteFrom(EventResponses.EVENT_RESPONSES)
                        .where(EventResponses.EVENT_RESPONSES.EVENT_ID.eq(postId).and(EventResponses.EVENT_RESPONSES.USER_ID.eq(id)))
                                .execute();

        dslContext.insertInto(EventResponses.EVENT_RESPONSES)
                .set(new EventResponsesRecord().setResponse(maybe)
                        .setEventId(postId)
                        .setUserId(id))
                .execute();
    }
}
