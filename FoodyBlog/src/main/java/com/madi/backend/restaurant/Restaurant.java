package com.madi.backend.restaurant;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.annotations.UuidGenerator;
import org.hibernate.validator.constraints.Range;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Entity
@Table(name = "Restaurant")
@Data
public class Restaurant {
    @Id
    @GeneratedValue
    @UuidGenerator
    private UUID id;

    @NotEmpty
    @Size(max = 100, message = "Max length of name is 100")
    private String name;

    @NotEmpty
    @Size(max = 300, message = "Max length of name is 300")
    private String address;

    @NotNull
    private Double longitude;

    @NotNull
    private Double latitude;

    @Range(min = 0, max = 5, message = "Stars must between 0 to 5")
    private int stars = 0;

    @NotEmpty
    @Size(min = 10, max = 10, message = "Length of phone number must be 10")
    private String phone;

    @NotEmpty
    @Size(max = 300, message = "Max length of name is 300")
    private String description;

    private List<String> categories;

    @CreationTimestamp
    private Date createdTime;

    @UpdateTimestamp
    private Date lastUpdatedTime;

}
