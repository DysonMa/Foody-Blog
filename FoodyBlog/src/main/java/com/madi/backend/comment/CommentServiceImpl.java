package com.madi.backend.comment;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.madi.backend.utils.queryParams.CustomSpecificationBulider;

@Service
public class CommentServiceImpl implements CommentService {
    @Autowired
    private CommentRepository commentRepo;

    @Transactional(readOnly = true)
    @Override
    public Comment getComment(UUID id) {
        Optional<Comment> comment = commentRepo.findById(id);
        if (!comment.isPresent()) {
            throw new ResourceNotFoundException("Comment " + id + " is not found");
        }
        return comment.get();
    }

    @Transactional(readOnly = true)
    @Override
    public List<Comment> filterComment(String filter) {
        CustomSpecificationBulider builder = new CustomSpecificationBulider();
        Pattern pattern = Pattern.compile("(\\w+?)(:|<|>)(\\w+?),");
        Matcher matcher = pattern.matcher(filter + ",");
        while (matcher.find()) {
            builder.with(
                    matcher.group(1),
                    matcher.group(2),
                    matcher.group(3));
        }
        Specification<Comment> spec = builder.build();
        return commentRepo.findAll(spec);
    }

    @Transactional(readOnly = true)
    @Override
    public List<Comment> getPublishedComments() {
        return commentRepo.findAll().stream().filter(c -> !c.getIsDraft()).toList();
    }

    @Transactional(readOnly = true)
    @Override
    public List<Comment> getDraftComments() {
        return commentRepo.findAll().stream().filter(c -> c.getIsDraft()).toList();
    }

    @Transactional(readOnly = true)
    @Override
    public List<Comment> listAllComments() {
        return commentRepo.findAll();
    }
}
