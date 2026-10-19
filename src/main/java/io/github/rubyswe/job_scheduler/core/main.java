package io.github.rubyswe.job_scheduler.core;
import io.github.rubyswe.job_scheduler.handlers.ImageSorter;

import java.io.File;
import java.io.IOException;

public class main {
    public static void main(String[] args) throws InterruptedException, IOException {

        // test 1

        /*
        JobQueue  jobQueue = new JobQueue();

        Thread thread = new Thread(new Worker(jobQueue));
        thread.start();

        Job job1 = new Job("Test-1", "Just a test", Job.Type.SORT_BY_RESOLUTION, "Image folder");
        jobQueue.submit(job1);
        System.out.println(jobQueue.get(job1.getId()).getStatus());

        Thread.sleep(2000);
        System.out.println(jobQueue.get(job1.getId()).getStatus());


        // Test 2: interrupt the worker mid-job
        Job job2 = new Job("Test-2", "Interrupted run", Job.Type.SORT_BY_RESOLUTION, "Image folder");
        jobQueue.submit(job2);

        Thread.sleep(500);    // worker is now in the middle of job2
        thread.interrupt();   // ask the worker to stop
        thread.join();        // wait for the worker thread to actually finish

        System.out.println("After interrupt: " + jobQueue.get(job2.getId()).getStatus());
        System.out.println("Worker alive? " + thread.isAlive());


        // test 2
         String TEST_FOLDER = "/Users/ruby/pic_test";
        ImageSorter imageSorter = new ImageSorter();
        int count = imageSorter.sortImages(TEST_FOLDER);


        // test 3

        // test 3: real jobs through the worker
        String TEST_FOLDER = "/Users/ruby/pic_test";

        JobQueue jobQueue = new JobQueue();
        Thread thread = new Thread(new Worker(jobQueue, new ImageSorter()));
        thread.start();

        Job good = new Job("Sort-1", "Real folder", Job.Type.SORT_BY_RESOLUTION, TEST_FOLDER);
        Job bad  = new Job("Sort-2", "Fake folder", Job.Type.SORT_BY_RESOLUTION, "/does/not/exist");
        jobQueue.submit(good);
        jobQueue.submit(bad);

        Thread.sleep(3000); // give the worker time to finish both

        System.out.println("Good job: " + jobQueue.get(good.getId()).getStatus());
        System.out.println("Bad job:  " + jobQueue.get(bad.getId()).getStatus());


        */






    }





}
