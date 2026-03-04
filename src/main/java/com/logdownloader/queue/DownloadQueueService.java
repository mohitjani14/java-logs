package com.logdownloader.queue;

import com.logdownloader.model.DownloadJob;
import org.springframework.stereotype.Service;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

@Service
public class DownloadQueueService {

    private final BlockingQueue<DownloadJob> queue = new LinkedBlockingQueue<>();

    public void addJob(DownloadJob job) {

        try {
            queue.put(job);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    // Added method to support new controller code
    public void enqueue(DownloadJob job) {
        addJob(job);
    }

    public DownloadJob takeJob() throws InterruptedException {
        return queue.take();
    }
}