package io.github.rubyswe.job_scheduler.endpoints;

import io.github.rubyswe.job_scheduler.core.Worker;
import org.jspecify.annotations.NonNull;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class WorkerStarter implements CommandLineRunner {

    private final Worker worker;
    public WorkerStarter(Worker worker) {
        this.worker = worker;
    }

    @Override
    public void run(String @NonNull ... args) throws Exception {
        new Thread(worker, "job-worker").start();
        System.out.println("Worker started");
    }
}
