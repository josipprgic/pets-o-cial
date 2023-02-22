package org.fer.hr.progi.nasiljubimci.service;

import lombok.RequiredArgsConstructor;
import org.fer.hr.progi.nasiljubimci.repository.PetsRepository;
import org.fer.hr.progi.nasiljubimci.repository.PostsRepository;
import org.fer.hr.progi.nasiljubimci.repository.UserRepository;
import org.fer.hr.progi.nasiljubimci.rest.model.Post;
import org.fer.hr.progi.nasiljubimci.rest.model.PostComment;
import org.fer.hr.progi.nasiljubimci.rest.model.PostCommentRequest;
import org.fer.hr.progi.nasiljubimci.rest.model.PostLikeRequest;
import org.fer.hr.progi.nasiljubimci.tables.records.PetsRecord;
import org.fer.hr.progi.nasiljubimci.tables.records.PostsRecord;
import org.fer.hr.progi.nasiljubimci.tables.records.UserRecord;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostsService {

    private final PostsRepository postsRepository;
    private final UserRepository userRepository;
    private final PetsRepository petsRepository;

    public List<Post> findPostsForUser(String username) {
        return userRepository.findFriendsForUser(username)
                .stream()
                .map(UserRecord::getId)
                .map(postsRepository::fetchTenPosts)
                .flatMap(List::stream)
                .collect(Collectors.toList());
    }

    public List<Post> findUserPosts(String username) {
        return postsRepository.fetchTenPosts(userRepository.findByUsername(username).getId());
    }

    public List<Post> findPostsForPet(Long petId) {
        return postsRepository.findPostsForPet(petId)
                .stream()
                .map(rec -> Post.of(rec, postsRepository.findPicture(rec.getContentId()), petsRepository.find(petId).getName(), userRepository.find(rec.getUserId()), Collections.emptyList(), rec.getCreatedat()))
                .collect(Collectors.toList());
    }

    public void uploadPost(byte[] bytes, String desc, String pet, String username) {
        Long userId = userRepository.findByUsername(username).getId();
        PetsRecord petsRecord = petsRepository.find(pet, userId);
        postsRepository.upload(bytes, desc, petsRecord == null ? null : petsRecord.getId(), userId);
    }

    public Post find(Long id) {
        PostsRecord rec = postsRepository.find(id);
        List<String> likes = postsRepository.findLikes(id);
         return Post.of(rec, postsRepository.findPicture(rec.getContentId()), petsRepository.find(rec.getPetId()).getName(), userRepository.find(rec.getUserId()), likes, rec.getCreatedat());
    }

    public void unlike(PostLikeRequest id) {
        postsRepository.unlike(id.getUserId(), id.getPostId());
    }

    public void like(PostLikeRequest id) {
        postsRepository.like(id.getUserId(), id.getPostId());
    }

    public void comment(Long userId, Long postId, String content) {
        postsRepository.comment(userId, postId, content);
    }

    public List<PostComment> comments(Long postId) {
        return postsRepository.comments(postId)
                .stream()
                .map(r -> new PostComment(r.getContent(), userRepository.find(r.getUserId())))
                .collect(Collectors.toList());
    }
}
