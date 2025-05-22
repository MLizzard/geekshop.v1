package com.study.geekshop.service.impl;

import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.Resource;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class LogServiceImplTest {

    private LogServiceImpl logService;

    @BeforeEach
    void setUp() {
        logService = new LogServiceImpl();
    }

    @Test
    void testValidateLogFileExists_FileExists() throws IOException {
        Path tempFile = Files.createTempFile("log", ".log");
        assertDoesNotThrow(() -> logService.validateLogFileExists(tempFile));
        Files.deleteIfExists(tempFile);
    }

    @Test
    void testCreateResourceFromTempFile_Success() throws IOException {
        Path file = Files.createTempFile("test-log", ".log");
        Files.writeString(file, "some log content");

        Resource resource = logService.createResourceFromTempFile(file, "14-04-2025");
        assertTrue(resource.exists());
        assertTrue(resource.getFilename().contains("test-log"));
    }

}
