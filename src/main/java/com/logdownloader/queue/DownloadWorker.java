package com.logdownloader.queue;

import com.logdownloader.model.Credential;
import com.logdownloader.model.DownloadJob;
import com.logdownloader.model.Module;
import com.logdownloader.model.Server;
import com.logdownloader.repository.DownloadJobRepository;
import com.logdownloader.sftp.SftpService;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

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

                    Module module = job.getModule();
                    Server server = module.getServer();
                    Credential credential = server.getCredential();

                    String host = server.getIp();
                    int port = server.getPort();
                    String username = credential.getUsername();
                    String password = credential.getPassword();
                    String remotePath = module.getLogPath();

                    String localPath = "/tmp/job_" + job.getId() + ".log";

                    sftpService.downloadFile(
                            host,
                            port,
                            username,
                            password,
                            remotePath,
                            localPath
                    );

                    job.setStatus("COMPLETED");
                    job.setFilePath(localPath);
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