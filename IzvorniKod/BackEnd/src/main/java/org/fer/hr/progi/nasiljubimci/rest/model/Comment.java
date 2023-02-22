package org.fer.hr.progi.nasiljubimci.rest.model;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class Comment {

    String username;
    String content;
}
