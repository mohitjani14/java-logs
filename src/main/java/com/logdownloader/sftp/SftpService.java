package com.logdownloader.sftp;

import net.schmizz.sshj.SSHClient;
import net.schmizz.sshj.sftp.RemoteResourceInfo;
import net.schmizz.sshj.sftp.SFTPClient;
import net.schmizz.sshj.transport.verification.PromiscuousVerifier;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SftpService {

    public List<RemoteResourceInfo> listFiles(String host,
                                              int port,
                                              String username,
                                              String password,
                                              String directory) {

        List<RemoteResourceInfo> files = new ArrayList<>();

        try {

            SSHClient sshClient = new SSHClient();
            sshClient.addHostKeyVerifier(new PromiscuousVerifier());

            sshClient.connect(host, port);
            sshClient.authPassword(username, password);

            try (SFTPClient sftpClient = sshClient.newSFTPClient()) {

                files = sftpClient.ls(directory);

            }

            sshClient.disconnect();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return files;
    }

    public void downloadFile(String host,
                             int port,
                             String username,
                             String password,
                             String remoteFile,
                             String localFile) {

        try {

            SSHClient sshClient = new SSHClient();
            sshClient.addHostKeyVerifier(new PromiscuousVerifier());

            sshClient.connect(host, port);
            sshClient.authPassword(username, password);

            try (SFTPClient sftpClient = sshClient.newSFTPClient()) {

                sftpClient.get(remoteFile, localFile);

            }

            sshClient.disconnect();

            System.out.println("Downloaded: " + remoteFile);

        } catch (Exception e) {

            e.printStackTrace();
        }
    }
}