package org.fer.hr.progi.nasiljubimci.rest.model;

import lombok.Value;

@Value
public class MessageDto {
    UserDto from;
    String sentAt;
    String content;
}
