package com.madi.backend.user;

import java.net.URI;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.madi.backend.comment.Comment;

@RestController
@RequestMapping(value = "users", produces = MediaType.APPLICATION_JSON_VALUE)
public class UserController {
    @Autowired
    private UserServiceImpl userSvc;

    @GetMapping("/{userId}")
    public ResponseEntity<User> getUser(@PathVariable("userId") UUID userId) {
        User user = userSvc.getUser(userId);
        if (user == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok().body(user);
    }

    @GetMapping
    public ResponseEntity<List<User>> searchUser(@RequestParam(name = "filter", required = false) String filter) {
        List<User> users = null;
        if (filter == null) {
            users = userSvc.listAllUsers();
        }
        return ResponseEntity.ok().body(users);
    }

    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody User newUser) {
        User user = userSvc.createUser(newUser);

        // Send this entity location back to user in Location header
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(user.getId())
                .toUri();

        return ResponseEntity.created(location).body(user);
    }

    @GetMapping("/{userId}/comments")
    public ResponseEntity<List<Comment>> getUserComments(@PathVariable("userId") UUID userId) {
        List<Comment> comments = userSvc.getUserComments(userId);
        return ResponseEntity.ok().body(comments);
    }

    @PostMapping("/{userId}/comments")
    public ResponseEntity<Comment> createUserComments(@PathVariable("userId") UUID userId,
            @RequestBody Comment requestBody) {
        Comment comment = userSvc.createUserComment(userId, requestBody);

        // Send this entity location back to user in Location header
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/comments/{id}")
                .buildAndExpand(comment.getId())
                .toUri();

        return ResponseEntity.created(location).body(comment);
    }

    @PatchMapping("/{userId}/comments/{commentId}")
    public ResponseEntity<Comment> updateUserComment(@PathVariable("userId") UUID userId,
            @PathVariable("commentId") UUID commentId, @RequestBody Comment newComment) {
        Comment comment = userSvc.updateUserComment(userId, commentId, newComment);
        return ResponseEntity.ok().body(comment);
    }

    @PatchMapping("/{userId}")
    public ResponseEntity<User> updateUser(@PathVariable("userId") UUID userId, @RequestBody User newUser) {
        User user = userSvc.updateUser(userId, newUser);
        return ResponseEntity.ok().body(user);
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<User> deleteUser(@PathVariable("userId") UUID userId) {
        userSvc.deleteUser(userId);
        return ResponseEntity.noContent().build();
    }
}
