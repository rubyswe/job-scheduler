package io.github.rubyswe.job_scheduler;

import io.github.rubyswe.job_scheduler.core.Job;
import io.github.rubyswe.job_scheduler.core.JobQueue;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;

@RestController // for testing
@SpringBootApplication
public class JobSchedulerApplication {

	public static void main(String[] args) {
		SpringApplication.run(JobSchedulerApplication.class, args);
	}

}
