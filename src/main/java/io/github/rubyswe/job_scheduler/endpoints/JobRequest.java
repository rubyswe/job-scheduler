package io.github.rubyswe.job_scheduler.endpoints;

import io.github.rubyswe.job_scheduler.core.Job;
import lombok.Data;

@Data
public class JobRequest {
    private String title;
    private String description;
    private Job.Type type;
    private String location;
}
