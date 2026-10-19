package io.github.rubyswe.job_scheduler.core;
import io.github.rubyswe.job_scheduler.handlers.ImageSorter;



public class Worker implements Runnable {

    private final JobQueue queue;
    private final ImageSorter imageSorter;


    public Worker(JobQueue queue, ImageSorter imageSorter) {
        this.queue = queue;
        this.imageSorter = imageSorter;
    }
    @Override
    public void run() {


            while (true) {
                // outer try block is for the worker being stopped

                try {
                    Job job = queue.take();
                    job.setStatus(Job.Status.RUNNING);
                    // inner try block is for the specific job
                    try  {
                        int count = 0;

                        System.out.println("Job " + job.getId() + " started");

                        switch (job.getType()) {
                            case SORT_BY_RESOLUTION -> count = imageSorter.sortImages(job.getLocation());
                            default -> throw new IllegalStateException("Unexpected value: " + job.getType());
                        }
                        System.out.println("Job " + job.getId() + " moved " + count + " images");
                        job.setStatus(Job.Status.COMPLETED);

                    } catch (Exception e) {
                        System.out.println("Job " + job.getId() + " failed: " + e.getMessage());
                        job.setStatus(Job.Status.FAILED);

                    }

                }catch (InterruptedException e){
                    Thread.currentThread().interrupt();
                    break;
                }
            }
    }
}
