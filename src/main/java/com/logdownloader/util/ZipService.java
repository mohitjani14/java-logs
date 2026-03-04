package com.logdownloader.util;

import org.springframework.stereotype.Service;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Service
public class ZipService {

    public void zipFiles(List<String> files, String zipFile) {

        try (ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(zipFile))) {

            for (String file : files) {

                Path path = Path.of(file);

                ZipEntry zipEntry = new ZipEntry(path.getFileName().toString());
                zos.putNextEntry(zipEntry);

                Files.copy(path, zos);

                zos.closeEntry();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}