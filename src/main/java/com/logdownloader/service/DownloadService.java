package com.logdownloader.service;

import com.logdownloader.model.DownloadJob;
import com.logdownloader.model.Module;
import com.logdownloader.queue.DownloadQueueService;
import com.logdownloader.repository.DownloadJobRepository;
import com.logdownloader.repository.ModuleRepository;
import org.springframework.stereotype.Service;

@Service
public class DownloadService {

    private final DownloadJobRepository downloadJobRepository;
    private final ModuleRepository moduleRepository;
    private final DownloadQueueService queueService;

    public DownloadService(DownloadJobRepository downloadJobRepository,
                           ModuleRepository moduleRepository,
                           DownloadQueueService queueService) {

        this.downloadJobRepository = downloadJobRepository;
        this.moduleRepository = moduleRepository;
        this.queueService = queueService;
    }

    public DownloadJob createJob(Long moduleId) {

        Module module = moduleRepository.findById(moduleId).orElseThrow();

        DownloadJob job = new DownloadJob();
        job.setModule(module);
        job.setStatus("QUEUED");

        job = downloadJobRepository.save(job);

        queueService.addJob(job);

        return job;
    }

    public DownloadJob getJob(Long jobId) {

        return downloadJobRepository.findById(jobId).orElseThrow();
    }
}