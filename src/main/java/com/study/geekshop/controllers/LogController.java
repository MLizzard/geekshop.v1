package com.study.geekshop.controllers;

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

    @Operation(summary = "Get .log file")
    @GetMapping("/download")
    public ResponseEntity<Resource> downloadLogs(
            @RequestParam String date
    ) {
        Resource logResource = logService.getLogsByDate(date);

        String filename = "logs-" + date + ".log";

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + filename)
                .contentType(MediaType.TEXT_PLAIN)
                .body(logResource);
    }

    @GetMapping("/prepare")
    public ResponseEntity<String> prepareLogs(@RequestParam String date) {
        UUID taskId = logService.prepareLogsAsync(date);
        return ResponseEntity.ok(taskId.toString());
    }

    @GetMapping("/status/{id}")
    public ResponseEntity<LogTaskInfo.Status> getStatus(@PathVariable UUID id) {
        return ResponseEntity.ok(logService.getLogStatus(id));
    }

    @GetMapping("/download/{id}")
    public ResponseEntity<Resource> downloadLogs(@PathVariable UUID id) {
        Resource resource = logService.getLogFile(id);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=logs-" + id + ".log")
                .contentType(MediaType.TEXT_PLAIN)
                .body(resource);
    }
}
