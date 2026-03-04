package com.logdownloader.service;

import com.logdownloader.model.DownloadJob;
import com.logdownloader.model.Module;
import com.logdownloader.repository.DownloadJobRepository;
import com.logdownloader.repository.ModuleRepository;
import org.springframework.stereotype.Service;

@Service
public class DownloadService {

    private final DownloadJobRepository downloadJobRepository;
    private final ModuleRepository moduleRepository;

    public DownloadService(DownloadJobRepository downloadJobRepository,
                           ModuleRepository moduleRepository) {
        this.downloadJobRepository = downloadJobRepository;
        this.moduleRepository = moduleRepository;
    }

    public DownloadJob createJob(Long moduleId) {

        Module module = moduleRepository.findById(moduleId).orElseThrow();

        DownloadJob job = new DownloadJob();
        job.setModule(module);
        job.setStatus("QUEUED");

        return downloadJobRepository.save(job);
    }

    public DownloadJob getJob(Long jobId) {
        return downloadJobRepository.findById(jobId).orElseThrow();
    }
}