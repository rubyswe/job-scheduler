package io.github.rubyswe.job_scheduler;

import io.github.rubyswe.job_scheduler.core.Job;
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


	// testing endpoints
	Job job = new Job("Test-1", "Just a test", Job.Type.SORT_BY_RESOLUTION, "Image folder");

	@GetMapping("/test")
	public String test() {

		ArrayList<Job> jobs = new ArrayList<>();
		jobs.add(job);

		return jobs.toString();
	}


}
