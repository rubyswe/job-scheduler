package io.github.rubyswe.job_scheduler.core;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

// Lombok to skip boilerplate code (setters, getters, and constructors )
@Data
@AllArgsConstructor

public class Job {

    // ------------ Enums for status and Types ------------ //
    public enum Status{
        PENDING,
        RUNNING,
        COMPLETED,
        FAILED
    }

    public enum Type{ // will add more
        SORT_BY_RESOLUTION
    }
    // --------------------------------------------------- //


    // -----------------Data fields-----------------------//
    private final String id; // cannot be changed
    private final LocalDateTime createdAt; // cannot be changed
    private String title;
    private String description;
    private Type type;
    private String location;
    private Status status;
    // --------------------------------------------------- //

    public Job(String title, String description, Type type, String location){
        this.title = title;
        this.description = description;
        this.type = type;
        this.location = location;

        createdAt = LocalDateTime.now();
        id = UUID.randomUUID().toString();
    }

}
