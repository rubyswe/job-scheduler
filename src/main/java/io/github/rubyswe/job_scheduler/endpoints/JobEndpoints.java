package io.github.rubyswe.job_scheduler.endpoints;

import io.github.rubyswe.job_scheduler.core.Job;
import io.github.rubyswe.job_scheduler.core.JobQueue;
import io.github.rubyswe.job_scheduler.core.Worker;
import io.github.rubyswe.job_scheduler.handlers.ImageSorter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class JobEndpoints {

    private final JobQueue jobQueue;

    public  JobEndpoints(JobQueue jobQueue) {
        this.jobQueue = jobQueue;
    }

    @PostMapping("/jobs")
    public ResponseEntity<String> createJob(@RequestBody JobRequest jobRequest) {

        Job job = new Job(jobRequest.getTitle(), jobRequest.getDescription(), jobRequest.getType(), jobRequest.getLocation());
        if (!jobQueue.submit(job)){
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body("Queue is full, try again later");
        }
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(job.getId());
    }

    @GetMapping("/jobs/{id}")
    public ResponseEntity<Job> getJob(@PathVariable String id) {

        Job job = jobQueue.get(id);
        if (jobQueue.get(id) == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(job);
    }

}
