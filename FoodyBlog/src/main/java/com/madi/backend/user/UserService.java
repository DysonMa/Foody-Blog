package com.madi.backend.user;

import java.util.List;
import java.util.UUID;

import com.madi.backend.comment.Comment;

public interface UserService {
    User getUser(UUID id);

    List<User> listAllUsers();

    User createUser(User requestBody);

    List<Comment> getUserComments(UUID userId);

    Comment createUserComment(UUID userId, Comment commentRequest);

    Comment updateUserComment(UUID userId, UUID commentId, Comment commentRequest);

    User updateUser(UUID userId, User newUser);

    void deleteUser(UUID userId);
}
