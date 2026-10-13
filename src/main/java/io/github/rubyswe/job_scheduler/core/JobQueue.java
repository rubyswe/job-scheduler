package io.github.rubyswe.job_scheduler.core;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;

public class JobQueue {

    private Map<String, Job> map = new HashMap<String, Job>();
    private ArrayBlockingQueue<Job> queue = new ArrayBlockingQueue<Job>(12);

    public void submit(Job job){
        // put job into map
        // offer job into queue
        map.put(job.getId(), job);
        queue.add(job);
    }

    public void take() throws InterruptedException {
        Job job = queue.take();
    }




}
