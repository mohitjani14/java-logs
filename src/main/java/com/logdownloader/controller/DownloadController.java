package com.logdownloader.controller;

import com.logdownloader.sftp.SftpService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/download")
public class DownloadController {

    private final SftpService sftpService;

    public DownloadController(SftpService sftpService) {
        this.sftpService = sftpService;
    }

    @PostMapping
    public String download() {

        sftpService.downloadFile(
                "192.168.1.11",         // server ip
                2222,
                "mohit",                // username
                "123",            // password
                "/var/log/app.log",    // remote file
                "/tmp/app.log"         // local file
        );

        return "Download started";
    }
}