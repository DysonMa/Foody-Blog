package com.madi.backend.user;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.madi.backend.comment.Comment;
import com.madi.backend.comment.CommentRepository;
import com.madi.backend.utils.security.PasswordUtils;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepo;

    @Autowired
    private CommentRepository commentRepo;

    @Transactional(readOnly = true)
    @Override
    public User getUser(UUID id) {
        Optional<User> user = userRepo.findById(id);
        if (user.isPresent()) {
            User existedUser = user.get();
            if (!existedUser.getSoftDeleted()) {
                return existedUser;
            }
        }
        return null;
    }

    @Transactional(readOnly = true)
    @Override
    public List<User> listAllUsers() {
        return userRepo.findAll().stream().filter(user -> !user.getSoftDeleted()).toList();
    }

    @Transactional
    @Override
    public User createUser(User requestBody) {
        User user = new User();
        user.setDisplayName(requestBody.getDisplayName());
        user.setEmail(requestBody.getEmail());
        user.setPassword(requestBody.getPassword());
        user.setAvatarId(requestBody.getAvatarId());
        user.setIntroduction(requestBody.getIntroduction());
        user.setSoftDeleted(false);

        return userRepo.save(user);
    }

    @Transactional(readOnly = true)
    @Override
    public List<Comment> getUserComments(UUID userId) {
        if (!userRepo.existsById(userId)) {
            throw new ResourceNotFoundException("No user found with id = " + userId);
        }
        return commentRepo.findByAuthorId(userId);
    }

    @Transactional
    @Override
    public Comment createUserComment(UUID userId, Comment commentRequest) {
        return userRepo.findById(userId).map(user -> {
            commentRequest.setAuthor(user);
            return commentRepo.save(commentRequest);
        }).orElseThrow(() -> new ResourceNotFoundException("No user found with id = " + userId));
    }

    @Transactional
    @Override
    public Comment updateUserComment(UUID userId, UUID commentId, Comment commentRequest) {
        Comment comment = commentRepo.findByAuthorId(userId).stream().filter(c -> c.getId().equals(commentId))
                .findFirst()
                .get();
        if (comment == null) {
            throw new ResourceNotFoundException(
                    "No comment found with userId " + userId + " and commentId " + commentId);
        }
        comment.setContent(commentRequest.getContent());
        comment.setImagesUrl(commentRequest.getImagesUrl());
        comment.setIsDraft(commentRequest.getIsDraft());
        comment.setTweetCount(commentRequest.getTweetCount());

        return commentRepo.save(comment);
    }

    @Transactional
    @Override
    public User updateUser(UUID userId, User newUser) {
        User user = getUser(userId);
        if (user == null) {
            throw new ResourceNotFoundException("No user found with id = " + userId);
        }

        user.setDisplayName(newUser.getDisplayName());
        user.setEmail(newUser.getEmail());
        user.setPassword(PasswordUtils.hashPassword(newUser.getPassword()));
        user.setAvatarId(newUser.getAvatarId());
        user.setIntroduction(newUser.getIntroduction());
        user.setSoftDeleted(false);

        return userRepo.save(user);
    }

    @Transactional
    @Override
    public void deleteUser(UUID userId) {
        User user = getUser(userId);
        if (user == null) {
            throw new ResourceNotFoundException("No user found with id = " + userId);
        }
        user.setSoftDeleted(true);
        userRepo.save(user);
    }

}
