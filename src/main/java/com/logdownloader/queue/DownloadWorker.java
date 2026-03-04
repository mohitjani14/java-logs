package com.logdownloader.queue;

import com.logdownloader.model.Credential;
import com.logdownloader.model.DownloadJob;
import com.logdownloader.model.Module;
import com.logdownloader.model.Server;
import com.logdownloader.repository.DownloadJobRepository;
import com.logdownloader.sftp.SftpService;
import jakarta.annotation.PostConstruct;
import net.schmizz.sshj.sftp.RemoteResourceInfo;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

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

        System.out.println("Download Worker Started");

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

                    String remoteDir = module.getLogPath();
                    LocalDate requestedDate = LocalDate.parse(job.getRequestedDate());

                    List<RemoteResourceInfo> files = sftpService.listFiles(
                            host,
                            port,
                            username,
                            password,
                            remoteDir
                    );

                    for (RemoteResourceInfo file : files) {

                        String name = file.getName();

                        if (name.equals(".") || name.equals("..")) {
                            continue;
                        }

                        boolean match = false;

                        if (name.contains(job.getRequestedDate())) {
                            match = true;
                        }

                        long mtime = file.getAttributes().getMtime();

                        LocalDate fileDate = Instant.ofEpochSecond(mtime)
                                .atZone(ZoneId.systemDefault())
                                .toLocalDate();

                        if (fileDate.equals(requestedDate)) {
                            match = true;
                        }

                        if (match) {

                            String remoteFile = remoteDir + "/" + name;
                            String localFile = "/tmp/job_" + job.getId() + "_" + name;

                            sftpService.downloadFile(
                                    host,
                                    port,
                                    username,
                                    password,
                                    remoteFile,
                                    localFile
                            );
                        }
                    }

                    job.setStatus("COMPLETED");
                    job.setCompletedAt(java.time.LocalDateTime.now());
                    job.setFilePath("/tmp");

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