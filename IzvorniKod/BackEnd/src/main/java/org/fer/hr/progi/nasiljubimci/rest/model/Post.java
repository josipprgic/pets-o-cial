package org.fer.hr.progi.nasiljubimci.rest.model;

import lombok.Builder;
import lombok.Value;
import org.fer.hr.progi.nasiljubimci.tables.records.PostsRecord;
import org.springframework.security.core.parameters.P;

import java.time.LocalDateTime;
import java.util.List;

@Value
@Builder
public class Post {

    long id;
    byte[] content;
    String description;
    String publisher;
    Integer numberOFLikes;
    Integer numberOfComments;
    String petName;
    List<String> likes;
    String createdAt;

    public static Post of(PostsRecord postsRecord, byte[] content, String petName, String username, List<String> likes, LocalDateTime localDateTime) {
        return new Post(postsRecord.getId(), content, postsRecord.getDescription(), username, 0, 0, petName, likes, localDateTime.toString());
    }
}
