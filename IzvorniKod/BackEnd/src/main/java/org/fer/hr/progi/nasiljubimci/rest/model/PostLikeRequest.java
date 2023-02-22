package org.fer.hr.progi.nasiljubimci.rest.model;

import lombok.Data;

@Data
public class PostLikeRequest {
    private Long userId;
    private Long postId;
}
