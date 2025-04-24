package com.study.geekshop.service.impl;

import com.study.geekshop.exceptions.InternalServerErrorException;
import com.study.geekshop.service.LogService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.file.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.zip.GZIPInputStream;

@Service
@RequiredArgsConstructor
public class LogServiceImpl implements LogService {

    private static final String LOGS_DIR = "logs/app.log/";
    private static final String CURRENT_LOG_FILE = "spring.log";

    @Override
    public Resource getLogsByDate(String date) {
        LocalDate logDate = parseDate(date);
        Path tempFile;

        if (logDate.equals(LocalDate.now())) {
            Path logFilePath = Paths.get(LOGS_DIR + CURRENT_LOG_FILE);
            validateLogFileExists(logFilePath);
            tempFile = copyCurrentLogToTempFile(logFilePath, logDate);
        } else {
            String archiveFileName = "spring.log." + logDate + ".0.gz";
            Path archivePath = Paths.get(LOGS_DIR + archiveFileName);
            validateLogFileExists(archivePath);
            tempFile = decompressGzipToTempFile(archivePath, logDate);
        }

        return createResourceFromTempFile(tempFile, date);
    }

    public LocalDate parseDate(String date) {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            return LocalDate.parse(date, formatter);
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Invalid date format. Required yyyy-MM-dd");
        }
    }

    public void validateLogFileExists(Path path) {
        if (!Files.exists(path)) {
            throw new EntityNotFoundException("Log file not found: " + path);
        }
    }

    public Path copyCurrentLogToTempFile(Path logFilePath, LocalDate logDate) {
        try {
            Path tempFile = Files.createTempFile("logs-" + logDate, ".log");
            Files.copy(logFilePath, tempFile, StandardCopyOption.REPLACE_EXISTING);
            return tempFile;
        } catch (IOException e) {
            throw new InternalServerErrorException("Error copying current log file: " + e.getMessage());
        }
    }

    public Path decompressGzipToTempFile(Path gzipPath, LocalDate logDate) {
        try {
            Path tempFile = Files.createTempFile("logs-" + logDate, ".log");

            try (GZIPInputStream gis = new GZIPInputStream(new FileInputStream(gzipPath.toFile()));
                 FileOutputStream fos = new FileOutputStream(tempFile.toFile())) {

                byte[] buffer = new byte[1024];
                int len;
                while ((len = gis.read(buffer)) > 0) {
                    fos.write(buffer, 0, len);
                }
            }

            return tempFile;
        } catch (IOException e) {
            throw new InternalServerErrorException("Failed to decompress log archive: " + e.getMessage());
        }
    }

    public Resource createResourceFromTempFile(Path tempFile, String date) {
        try {
            if (Files.size(tempFile) == 0) {
                throw new EntityNotFoundException("No logs found for " + date);
            }
            Resource resource = new UrlResource(tempFile.toUri());
            tempFile.toFile().deleteOnExit();
            return resource;
        } catch (IOException e) {
            throw new InternalServerErrorException("Error creating resource from log file: " + e.getMessage());
        }
    }
}
