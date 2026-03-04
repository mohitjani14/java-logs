package com.logdownloader.sftp;

import net.schmizz.sshj.SSHClient;
import net.schmizz.sshj.sftp.SFTPClient;
import net.schmizz.sshj.transport.verification.PromiscuousVerifier;
import org.springframework.stereotype.Service;

import java.io.FileOutputStream;

@Service
public class SftpService {

    public void downloadFile(String host,
                             int port,
                             String username,
                             String password,
                             String remotePath,
                             String localPath) {

        try {

            SSHClient sshClient = new SSHClient();
            sshClient.addHostKeyVerifier(new PromiscuousVerifier());

            sshClient.connect(host, port);

            sshClient.authPassword(username, password);

            SFTPClient sftpClient = sshClient.newSFTPClient();

            FileOutputStream fos = new FileOutputStream(localPath);

            sftpClient.get(remotePath, fos);

            fos.close();
            sftpClient.close();
            sshClient.disconnect();

            System.out.println("File downloaded successfully");

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}