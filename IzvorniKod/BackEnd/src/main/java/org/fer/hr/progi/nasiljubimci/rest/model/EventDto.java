package org.fer.hr.progi.nasiljubimci.rest.model;

import lombok.Value;
import org.fer.hr.progi.nasiljubimci.tables.records.EventsRecord;

import java.util.List;

@Value
public class EventDto {

    Long id;
    String organizer;
    String description;
    String location;
    String startsAt;
    Long duration;
    List<String> coming;

    public static EventDto from(EventsRecord record, List<String> coming, String organizer) {
        return new EventDto(record.getId(), organizer, record.getDescription(), record.getLocation(), record.getStartDate().toString(), record.getDuration().toDuration().toHours(), coming);
    }
}
