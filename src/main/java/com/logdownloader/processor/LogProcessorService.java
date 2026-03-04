package com.logdownloader.processor;

import org.springframework.stereotype.Service;

import java.io.*;
import java.time.LocalDate;

@Service
public class LogProcessorService {

    public String filterLogsByDate(String inputFile,
                                   LocalDate date,
                                   String outputFile) {

        try (BufferedReader reader = new BufferedReader(new FileReader(inputFile));
             BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile))) {

            String line;
            String dateString = date.toString();

            while ((line = reader.readLine()) != null) {

                if (line.contains(dateString)) {

                    writer.write(line);
                    writer.newLine();
                }
            }

        } catch (Exception e) {

            e.printStackTrace();
        }

        return outputFile;
    }
}