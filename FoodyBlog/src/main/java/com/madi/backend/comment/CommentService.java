package com.madi.backend.comment;

import java.util.List;
import java.util.UUID;

public interface CommentService {
    Comment getComment(UUID id);

    List<Comment> filterComment(String filter);

    List<Comment> getPublishedComments();

    List<Comment> getDraftComments();

    List<Comment> listAllComments();
}
