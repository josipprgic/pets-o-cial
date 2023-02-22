package org.fer.hr.progi.nasiljubimci.rest.model;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class AddEventRequest {
    private String description;
    private LocalDate startDate;
    private LocalTime startTime;
    private Integer duration;
    private String location;
}
