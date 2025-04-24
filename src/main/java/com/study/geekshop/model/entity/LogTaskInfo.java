package com.study.geekshop.model.entity;

import java.nio.file.Path;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LogTaskInfo {
    public enum Status { PENDING, READY, FAILED }

    private final String date;

    private volatile Status status;

    private Path file;

    private volatile String errorMessage;

    public LogTaskInfo(String date) {
        this.date = date;
    }
}
