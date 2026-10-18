package io.github.rubyswe.job_scheduler.core;



public class Worker implements Runnable {

    private final JobQueue queue;
    public Worker(JobQueue queue) {
        this.queue = queue;
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

                        System.out.println("Job " + job.getId() + " started");
                        Thread.sleep(1000);
                        job.setStatus(Job.Status.COMPLETED);

                    } catch (InterruptedException e ) {
                        System.out.println("Job " + job.getId() + " interrupted");
                        job.setStatus(Job.Status.PENDING); // for job being stopped halve way.
                        throw e;
                    }
                    catch (Exception e) {
                        job.setStatus(Job.Status.FAILED);
                    }

                }catch (InterruptedException e){
                    Thread.currentThread().interrupt();
                    break;
                }
            }
    }
}
