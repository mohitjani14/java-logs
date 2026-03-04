package com.logdownloader.controller;

import com.logdownloader.model.DownloadJob;
import com.logdownloader.service.DownloadService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/download")
public class DownloadController {

    private final DownloadService downloadService;

    public DownloadController(DownloadService downloadService) {
        this.downloadService = downloadService;
    }

    @PostMapping("/{moduleId}")
    public DownloadJob startDownload(@PathVariable Long moduleId) {

        return downloadService.createJob(moduleId);
    }

    @GetMapping("/status/{jobId}")
    public DownloadJob getStatus(@PathVariable Long jobId) {

        return downloadService.getJob(jobId);
    }
}