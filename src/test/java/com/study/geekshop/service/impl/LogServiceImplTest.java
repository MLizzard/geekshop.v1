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
    void testParseDate_Valid() {
        LocalDate date = logService.parseDate("14-04-2025");
        assertEquals(LocalDate.of(2025, 4, 14), date);
    }

    @Test
    void testParseDate_InvalidFormat() {
        Exception ex = assertThrows(IllegalArgumentException.class,
                () -> logService.parseDate("2025-04-14"));
        assertEquals("Invalid date format. Required dd-MM-yyyy", ex.getMessage());
    }

    @Test
    void testValidateLogFileExists_FileExists() throws IOException {
        Path tempFile = Files.createTempFile("log", ".log");
        assertDoesNotThrow(() -> logService.validateLogFileExists(tempFile));
        Files.deleteIfExists(tempFile);
    }

    @Test
    void testValidateLogFileExists_FileNotExist() {
        Path path = Paths.get("nonexistent.log");
        EntityNotFoundException ex = assertThrows(EntityNotFoundException.class,
                () -> logService.validateLogFileExists(path));
        assertTrue(ex.getMessage().contains("File doesn't exist"));
    }


    @Test
    void testFilterAndWriteLogsToTempFile() throws IOException {
        Path source = Files.createTempFile("logsource", ".log");
        Files.write(source, """
                14-04-2025 INFO something
                13-04-2025 INFO ignore
                14-04-2025 ERROR match
                """.getBytes());

        Path target = Files.createTempFile("filtered", ".log");

        //logService.filterAndWriteLogsToTempFile(source, "14-04-2025", target);

        var lines = Files.readAllLines(target);
        assertEquals(2, lines.size());
        assertTrue(lines.get(0).contains("14-04-2025"));

        Files.deleteIfExists(source);
        Files.deleteIfExists(target);
    }

    @Test
    void testCreateResourceFromTempFile_Success() throws IOException {
        Path file = Files.createTempFile("test-log", ".log");
        Files.writeString(file, "some log content");

        Resource resource = logService.createResourceFromTempFile(file, "14-04-2025");
        assertTrue(resource.exists());
        assertTrue(resource.getFilename().contains("test-log"));
    }

    @Test
    void testCreateResourceFromTempFile_EmptyFile() throws IOException {
        Path file = Files.createTempFile("empty", ".log");

        EntityNotFoundException ex = assertThrows(EntityNotFoundException.class,
                () -> logService.createResourceFromTempFile(file, "14-04-2025"));
        assertTrue(ex.getMessage().contains("There are no logs for specified date"));
    }

    @Test
    void testGetLogsByDate_Success() throws IOException {
        // Arrange
        String date = "14-04-2025";
        Path logDir = Paths.get("logs/app.log");
        Files.createDirectories(logDir);

        Path logFilePath = logDir.resolve("spring.log");
        Files.write(logFilePath, """
        14-04-2025 INFO This should be included
        13-04-2025 INFO This should be ignored
        14-04-2025 ERROR This should also be included
    """.getBytes());

        // Act
        Resource resource = logService.getLogsByDate(date);

        // Assert
        assertNotNull(resource);
        assertTrue(resource.exists());

        var content = new String(resource.getInputStream().readAllBytes());
        assertTrue(content.contains("14-04-2025 INFO This should be included"));
        assertFalse(content.contains("13-04-2025 INFO This should be ignored"));

        // Clean up
        Files.deleteIfExists(logFilePath);
    }
}
