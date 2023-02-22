package org.fer.hr.progi.nasiljubimci.rest.model;

import lombok.Data;

@Data
public class SendMessageRequest {
    private String toUser;
    private String content;
}
