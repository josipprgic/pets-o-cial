package org.fer.hr.progi.nasiljubimci.rest.controller;

import lombok.RequiredArgsConstructor;
import org.fer.hr.progi.nasiljubimci.rest.model.*;
import org.fer.hr.progi.nasiljubimci.service.PostsService;
import org.fer.hr.progi.nasiljubimci.service.UserService;
import org.fer.hr.progi.nasiljubimci.tables.records.PostsRecord;
import org.springframework.http.MediaType;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class PostsController {

    private final PostsService postsService;
    private final UserService userService;

    @GetMapping("/posts/feed/{username}")
    public List<Post> getFeedForUser(@PathVariable String username) {

        return postsService.findPostsForUser(username).stream()
                .filter(p -> !userService.getBlockedStatus(p.getPublisher(), username).isBlocked())
                .collect(Collectors.toList());
    }

    @GetMapping("/posts/profile/{username}")
    public List<Post> getPostsForUser(@PathVariable String username) {
        String user = ((UserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
        if (userService.getBlockedStatus(username, user).isBlocked()) {
            return null;
        }
        return postsService.findUserPosts(username);
    }

    @GetMapping("/posts/pets/{petId}")
    public List<Post> getPosts(@PathVariable Long petId) {
        return postsService.findPostsForPet(petId);
    }

    @PostMapping(path = "/posts/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public void upload(@RequestParam("file") MultipartFile file, @RequestParam String name,
                       @RequestParam String desc, @RequestParam String pet) throws IOException {
        postsService.uploadPost(file.getBytes(), desc, pet, ((UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername());
    }

    @GetMapping("/posts")
    public Post get(@RequestParam Long id) {
        Post p = postsService.find(id);
        String user = ((UserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
        if (userService.getBlockedStatus(p.getPublisher(), user).isBlocked()) {
            return null;
        }
        return p;
    }

    @PostMapping("/posts/like")
    public void like(@RequestBody PostLikeRequest id) {
         postsService.like(id);
    }

    @PostMapping("/posts/unlike")
    public void unlike(@RequestBody PostLikeRequest id) {
         postsService.unlike(id);
    }

    @PostMapping("/posts/uploadComment")
    public void comment(@RequestBody PostCommentRequest id) {
        Long userId = userService.findByUsername(((UserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername()).getId();
        postsService.comment(userId, id.getPostId(), id.getContent());
    }

    @GetMapping("/posts/comments/{postId}")
    public List<PostComment> comment(@PathVariable Long postId) {
        return postsService.comments(postId);
    }
}
