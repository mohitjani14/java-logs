package com.logdownloader.queue;

import com.logdownloader.model.DownloadJob;
import com.logdownloader.repository.DownloadJobRepository;
import com.logdownloader.sftp.SftpService;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;
import java.time.LocalDateTime;

@Component
public class DownloadWorker {

    private final DownloadQueueService queueService;
    private final DownloadJobRepository jobRepository;
    private final SftpService sftpService;

    public DownloadWorker(DownloadQueueService queueService,
                          DownloadJobRepository jobRepository,
                          SftpService sftpService) {

        this.queueService = queueService;
        this.jobRepository = jobRepository;
        this.sftpService = sftpService;
    }

    @PostConstruct
    public void startWorker() {

        Thread worker = new Thread(() -> {

            while (true) {

                try {

                    DownloadJob job = queueService.takeJob();

                    job.setStatus("RUNNING");
                    jobRepository.save(job);

                    // Example download (temporary)
                    sftpService.downloadFile(
                            "192.168.7.11",
                            2222,
                            "mohit",
                            "123",
                            "/var/log/app.log",
                            "/tmp/job_" + job.getId() + ".log"
                    );

                    job.setStatus("COMPLETED");
                    job.setFilePath("/tmp/job_" + job.getId() + ".log");
                    job.setCompletedAt(LocalDateTime.now());

                    jobRepository.save(job);

                } catch (Exception e) {

                    e.printStackTrace();
                }
            }

        });

        worker.setDaemon(true);
        worker.start();
    }
}