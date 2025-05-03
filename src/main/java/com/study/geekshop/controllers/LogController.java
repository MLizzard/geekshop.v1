package com.study.geekshop.controllers;

import com.study.geekshop.exceptions.LogNotReadyException;
import com.study.geekshop.model.entity.LogTaskInfo;
import com.study.geekshop.service.LogService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.UUID;


@RestController
@RequestMapping("/logs")
@Tag(name = "Log controller", description = "Operations with .log files")
@RequiredArgsConstructor
public class LogController {

    private final LogService logService;

    @Operation(summary = "Get .log file (synchronous)")
    @GetMapping("/download")
    public ResponseEntity<Resource> downloadLogs(@RequestParam String date) {
        Resource logResource = logService.getLogsByDate(date);
        String filename = "logs-" + date + ".log";

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + filename)
                .contentType(MediaType.TEXT_PLAIN)
                .body(logResource);
    }

    @Operation(summary = "Start async log preparation")
    @GetMapping("/prepare")
    public ResponseEntity<String> prepareLogs(@RequestParam String date) {
        UUID taskId = logService.prepareLogsAsync(date);
        return ResponseEntity.accepted().body(taskId.toString()); // 202 Accepted
    }

    @Operation(summary = "Get status of async log preparation")
    @GetMapping("/status/{id}")
    public ResponseEntity<LogTaskInfo.Status> getStatus(@PathVariable UUID id) {
        return ResponseEntity.ok(logService.getLogStatus(id));
    }

    @Operation(summary = "Download async prepared log file by ID")
    @GetMapping("/download/{id}")
    public ResponseEntity<?> downloadLogs(@PathVariable UUID id) {
        try {
            Resource resource = logService.getLogFile(id);
            String filename = "logs-" + id + ".log";

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + filename)
                    .contentType(MediaType.TEXT_PLAIN)
                    .body(resource);
        } catch (LogNotReadyException e) {
            return ResponseEntity.status(202).body("Log is still being prepared. Please try again later.");
        }
    }
}
