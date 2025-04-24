package com.study.geekshop.service;

import java.nio.file.Path;
import java.time.LocalDate;
import java.util.UUID;

import com.study.geekshop.model.entity.LogTaskInfo;
import org.springframework.core.io.Resource;

public interface LogService {
    Resource getLogsByDate(String date);

    LocalDate parseDate(String date);

    void validateLogFileExists(Path path);

    Resource createResourceFromTempFile(Path tempFile, String date);

    UUID prepareLogsAsync(String date);

    LogTaskInfo.Status getLogStatus(UUID id);

    Resource getLogFile(UUID id);

}