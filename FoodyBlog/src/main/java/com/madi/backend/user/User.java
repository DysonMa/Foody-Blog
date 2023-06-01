package com.madi.backend.user;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.annotations.UuidGenerator;

import com.madi.backend.comment.Comment;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Entity
@Table(name = "Users")
@Data
public class User {
    @Id
    @GeneratedValue
    @UuidGenerator
    private UUID id;

    @NotEmpty
    @Size(max = 100, message = "Max length of name is 100")
    private String displayName;

    @NotEmpty
    @Email
    private String email;

    @NotEmpty
    private String password;

    private String avatarId;

    @Size(max = 500)
    private String introduction;

    private Boolean softDeleted = false;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "author")
    private List<Comment> comment;

    @CreationTimestamp
    private Date createdTime;

    @UpdateTimestamp
    private Date lastUpdatedTime;

}
