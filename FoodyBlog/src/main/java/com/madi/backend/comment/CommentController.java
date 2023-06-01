package com.madi.backend.comment;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "comments", produces = MediaType.APPLICATION_JSON_VALUE)
public class CommentController {
    @Autowired
    private CommentServiceImpl commentSvc;

    @GetMapping("/{id}")
    public ResponseEntity<Comment> getComment(@PathVariable("id") UUID id) {
        Comment comment = commentSvc.getComment(id);
        if (comment == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok().body(comment);
    }

    @GetMapping("/drafts")
    public ResponseEntity<List<Comment>> getDraftComments() {
        List<Comment> comments = commentSvc.getDraftComments();
        return ResponseEntity.ok().body(comments);
    }

    @GetMapping("/published")
    public ResponseEntity<List<Comment>> getPublishedComments() {
        List<Comment> comments = commentSvc.getPublishedComments();
        return ResponseEntity.ok().body(comments);
    }

    @GetMapping
    public ResponseEntity<List<Comment>> searchUser(@RequestParam(name = "filter", required = false) String filter) {
        List<Comment> comments = null;
        if (filter == null) {
            comments = commentSvc.listAllComments();
        } else {
            comments = commentSvc.filterComment(filter);
        }
        return ResponseEntity.ok().body(comments);
    }
}
