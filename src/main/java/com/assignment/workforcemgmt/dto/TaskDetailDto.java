package com.assignment.workforcemgmt.dto;

import lombok.Data;

import java.util.List;

@Data
public class TaskDetailDto {
    private TaskManagementDto task;
    private List<CommentDto> comments;
    private List<ActivityLogDto> activityLogs;
}