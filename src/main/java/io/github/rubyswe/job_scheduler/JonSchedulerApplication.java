package io.github.rubyswe.job_scheduler;

import io.github.rubyswe.job_scheduler.core.Job;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.time.LocalDateTime;

@SpringBootApplication
public class JonSchedulerApplication {

	public static void main(String[] args) {
		SpringApplication.run(JonSchedulerApplication.class, args);
	}


}
