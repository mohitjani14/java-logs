package com.logdownloader.queue;

import com.logdownloader.model.Credential;
import com.logdownloader.model.DownloadJob;
import com.logdownloader.model.Module;
import com.logdownloader.model.Server;
import com.logdownloader.repository.DownloadJobRepository;
import com.logdownloader.sftp.SftpService;
import com.logdownloader.service.LogProcessorService;

import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Component
public class DownloadWorker {

    private final DownloadQueueService queueService;
    private final DownloadJobRepository jobRepository;
    private final SftpService sftpService;
    private final LogProcessorService processor;

    public DownloadWorker(DownloadQueueService queueService,
                          DownloadJobRepository jobRepository,
                          SftpService sftpService,
                          LogProcessorService processor) {

        this.queueService = queueService;
        this.jobRepository = jobRepository;
        this.sftpService = sftpService;
        this.processor = processor;
    }

    @PostConstruct
    public void startWorker() {

        Thread worker = new Thread(() -> {

            while (true) {

                DownloadJob job = null;

                try {

                    job = queueService.takeJob();

                    job.setStatus("RUNNING");
                    jobRepository.save(job);

                    Module module = job.getModule();
                    Server server = module.getServer();
                    Credential credential = server.getCredential();

                    String host = server.getIp();
                    int port = server.getPort();
                    String username = credential.getUsername();
                    String password = credential.getPassword();
                    String remotePath = module.getLogPath();

                    String localPath = "/tmp/job_" + job.getId() + ".log";

                    // Download logs via SFTP
                    sftpService.downloadFile(
                            host,
                            port,
                            username,
                            password,
                            remotePath,
                            localPath
                    );

                    // Filter logs by requested date
                    String filteredFile = "/tmp/job_" + job.getId() + "_filtered.log";

                    processor.filterLogsByDate(
                            localPath,
                            LocalDate.parse(job.getRequestedDate()),
                            filteredFile
                    );

                    job.setStatus("COMPLETED");
                    job.setFilePath(filteredFile);
                    job.setCompletedAt(LocalDateTime.now());

                    jobRepository.save(job);

                } catch (Exception e) {

                    e.printStackTrace();

                    if (job != null) {
                        job.setStatus("FAILED");
                        jobRepository.save(job);
                    }
                }
            }

        });

        worker.setDaemon(true);
        worker.start();
    }
}