package io.github.rubyswe.job_scheduler.core;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ConcurrentHashMap;

public class JobQueue {

    private static final int CAPACITY = 12; // shared across prog
    private final Map<String, Job> map = new ConcurrentHashMap<>();
    private static final ArrayBlockingQueue<Job> queue = new ArrayBlockingQueue<>(CAPACITY);

    public boolean submit(Job job){
        // will return bool depending on if queue is full or not
        // only put job into map if queue succeeds
        if (queue.offer(job)){
            map.put(job.getId(), job);
            return true;
        }
        return false;
    }

    public Job take() throws InterruptedException {
        return queue.take();
    }

    public Job get(String id){
        return map.get(id);
    }
}
