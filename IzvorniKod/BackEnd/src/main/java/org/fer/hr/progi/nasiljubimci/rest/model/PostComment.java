package org.fer.hr.progi.nasiljubimci.rest.model;

import lombok.Builder;
import lombok.Value;

@Value
public class PostComment {
    String content;
    String username;
}
