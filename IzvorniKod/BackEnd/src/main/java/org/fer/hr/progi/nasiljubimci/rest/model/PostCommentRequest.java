package org.fer.hr.progi.nasiljubimci.rest.model;

import lombok.Data;

@Data
public class PostCommentRequest {
    private Long postId;
    private String content;
}
