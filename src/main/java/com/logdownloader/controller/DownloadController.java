package com.logdownloader.controller;

import com.logdownloader.model.DownloadJob;
import com.logdownloader.model.Module;
import com.logdownloader.repository.DownloadJobRepository;
import com.logdownloader.repository.ModuleRepository;
import com.logdownloader.queue.DownloadQueueService;
import com.logdownloader.service.DownloadService;

import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;

import java.io.File;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/download")
public class DownloadController {

    private final DownloadService downloadService;

    @Autowired
    private DownloadJobRepository jobRepository;

    @Autowired
    private ModuleRepository moduleRepository;

    @Autowired
    private DownloadQueueService downloadQueueService;

    public DownloadController(DownloadService downloadService) {
        this.downloadService = downloadService;
    }

    @PostMapping("/{moduleId}")
    public DownloadJob startDownload(
            @PathVariable Long moduleId,
            @RequestParam String from,
            @RequestParam String to) {

        Module module = moduleRepository.findById(moduleId)
                .orElseThrow(() -> new RuntimeException("Module not found"));

        DownloadJob job = new DownloadJob();

        job.setModule(module);
        job.setRequestedDate(from); // compatibility
        job.setFromDate(from);
        job.setToDate(to);
        job.setStatus("QUEUED");
        job.setCreatedAt(LocalDateTime.now());

        jobRepository.save(job);

        downloadQueueService.enqueue(job);

        return job;
    }

    @GetMapping("/status/{jobId}")
    public DownloadJob getStatus(@PathVariable Long jobId) {

        return downloadService.getJob(jobId);
    }

    @GetMapping("/file/{jobId}")
    public ResponseEntity<Resource> downloadFile(@PathVariable Long jobId) throws Exception {

        DownloadJob job = jobRepository.findById(jobId)
                .orElseThrow(() -> new RuntimeException("Job not found"));

        String filePath = job.getFilePath();

        File file = new File(filePath);

        Resource resource = new UrlResource(file.toURI());

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=" + file.getName())
                .body(resource);
    }
}