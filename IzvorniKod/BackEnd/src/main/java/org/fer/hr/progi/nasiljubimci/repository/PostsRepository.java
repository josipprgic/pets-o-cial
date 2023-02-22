package org.fer.hr.progi.nasiljubimci.repository;

import lombok.RequiredArgsConstructor;
import org.fer.hr.progi.nasiljubimci.rest.model.Post;
import org.fer.hr.progi.nasiljubimci.rest.model.PostCommentRequest;
import org.fer.hr.progi.nasiljubimci.tables.*;
import org.fer.hr.progi.nasiljubimci.tables.records.*;
import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class PostsRepository {

    private final DSLContext dslContext;

    public List<Post> fetchTenPosts(Long id) {
        return dslContext.selectFrom(Posts.POSTS)
                .where(Posts.POSTS.USER_ID.eq(id))
                .limit(10)
                .fetchInto(Posts.POSTS)
                .stream()
                .map(r -> Post.of(
                        r,
                        dslContext.selectFrom(Media.MEDIA)
                                .where(Media.MEDIA.ID.eq(r.getContentId()))
                                .fetchOne(Media.MEDIA.CONTENT),
                        dslContext.selectFrom(Pets.PETS)
                                .where(Pets.PETS.ID.eq(r.getPetId()))
                                .fetchOne(Pets.PETS.NAME),
                        dslContext.selectFrom(User.USER)
                                .where(User.USER.ID.eq(r.getUserId()))
                                .fetchOne(User.USER.USERNAME),
                        Collections.emptyList(),
                        r.getCreatedat()
                ))
                .collect(Collectors.toList());
    }

    public byte[] findPicture(Long profilePictureId) {
        return dslContext.selectFrom(Media.MEDIA)
                .where(Media.MEDIA.ID.eq(profilePictureId))
                .fetchOne(Media.MEDIA.CONTENT);
    }

    public List<PostsRecord> findPostsForPet(Long petId) {
        return dslContext.selectFrom(Posts.POSTS)
                .where(Posts.POSTS.PET_ID.eq(petId))
                .fetchInto(Posts.POSTS);
    }

    public void upload(byte[] bytes, String desc, Long petId, Long userId) {
        long picid = dslContext.insertInto(Media.MEDIA)
                .set(new MediaRecord().setContent(bytes))
                .returning(Media.MEDIA.ID)
                .fetchOne().getId();

        dslContext.insertInto(Posts.POSTS)
                .set(new PostsRecord().setContentId(picid)
                        .setDescription(desc)
                        .setUserId(userId)
                        .setPetId(petId))
                .execute();
    }

    public PostsRecord find(Long id) {
        return dslContext.selectFrom(Posts.POSTS)
                .where(Posts.POSTS.ID.eq(id))
                .fetchOne();
    }

    public List<String> findLikes(Long id) {
        return dslContext.selectFrom(Likes.LIKES.join(User.USER).on(Likes.LIKES.USER_ID.eq(User.USER.ID)))
                .where(Likes.LIKES.POST_ID.eq(id))
                .fetchInto(User.USER).stream().map(UserRecord::getUsername).collect(Collectors.toList());
    }

    public void like(Long userId, Long postId) {
        dslContext.insertInto(Likes.LIKES)
                .set(new LikesRecord().setUserId(userId).setPostId(postId))
                .execute();
    }

    public void unlike(Long userId, Long postId) {
        dslContext.deleteFrom(Likes.LIKES)
                .where(Likes.LIKES.USER_ID.eq(userId).and(Likes.LIKES.POST_ID.eq(postId)))
                .execute();
    }

    public void comment(Long userId, Long postId, String content) {
        dslContext.insertInto(Comments.COMMENTS)
                .set(new CommentsRecord().setContent(content)
                        .setUserId(userId)
                        .setPostId(postId))
                .execute();
    }

    public List<CommentsRecord> comments(Long postId) {
        return dslContext.selectFrom(Comments.COMMENTS)
                .where(Comments.COMMENTS.POST_ID.eq(postId))
                .fetchInto(Comments.COMMENTS);
    }
}
