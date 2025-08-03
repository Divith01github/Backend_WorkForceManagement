package com.assignment.workforcemgmt.model;
import lombok.Data;

@Data
public class ActivityLog {
    private Long id;
    private Long taskId;
    private String message;
    private Long timestamp;
}