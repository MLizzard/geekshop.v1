package com.study.geekshop.service.impl;

import com.study.geekshop.exceptions.InternalServerErrorException;
import com.study.geekshop.exceptions.LogNotReadyException;
import com.study.geekshop.model.entity.LogTaskInfo;
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
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.zip.GZIPInputStream;

@Service
@RequiredArgsConstructor
public class LogServiceImpl implements LogService {

    private static final String LOGS_DIR = "logs/app.log/";
    private static final String CURRENT_LOG_FILE = "spring.log";

    private final ExecutorService executor = Executors.newCachedThreadPool();
    private final Map<UUID, LogTaskInfo> tasks = new ConcurrentHashMap<>();

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

    @Override
    public UUID prepareLogsAsync(String date) {
        UUID id = UUID.randomUUID();
        LogTaskInfo taskInfo = new LogTaskInfo(date);
        tasks.put(id, taskInfo);

        executor.submit(() -> {
            try {
                taskInfo.setStatus(LogTaskInfo.Status.PENDING);
                Thread.sleep(10000); // artificial delay
                LocalDate logDate = parseDate(date);
                Path file;
                if (logDate.equals(LocalDate.now())) {
                    file = copyCurrentLogToTempFile(Paths.get(LOGS_DIR + CURRENT_LOG_FILE), logDate);
                } else {
                    file = decompressGzipToTempFile(Paths.get(LOGS_DIR + "spring.log." + logDate + ".0.gz"), logDate);
                }
                taskInfo.setFile(file);
                taskInfo.setStatus(LogTaskInfo.Status.READY);
            } catch (Exception e) {
                taskInfo.setStatus(LogTaskInfo.Status.FAILED);
                taskInfo.setErrorMessage(e.getMessage());
            }
        });

        return id;
    }

    @Override
    public LogTaskInfo.Status getLogStatus(UUID id) {
        LogTaskInfo info = tasks.get(id);
        if (info == null) throw new EntityNotFoundException("No such task: " + id);
        return info.getStatus();
    }

    @Override
    public Resource getLogFile(UUID id) {
        LogTaskInfo info = tasks.get(id);
        if (info == null) throw new EntityNotFoundException("No such task: " + id);

        if (info.getStatus() == LogTaskInfo.Status.FAILED)
            throw new InternalServerErrorException("Log task failed: " + info.getErrorMessage());

        if (info.getStatus() != LogTaskInfo.Status.READY)
            throw new LogNotReadyException("Log is still being prepared");

        try {
            return new UrlResource(info.getFile().toUri());
        } catch (IOException e) {
            throw new InternalServerErrorException("Unable to load file: " + e.getMessage());
        }
    }
}
