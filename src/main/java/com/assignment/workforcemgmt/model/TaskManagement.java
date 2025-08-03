package com.assignment.workforcemgmt.model;

import com.assignment.workforcemgmt.common.model.enums.ReferenceType;
import com.assignment.workforcemgmt.model.enums.Priority;
import com.assignment.workforcemgmt.model.enums.TaskStatus;
import lombok.Data;

@Data
public class TaskManagement {
    private Long id;
    private Long referenceId;
    private ReferenceType referenceType;
    private Task task;
    private String description;
    private TaskStatus status;
    private Long assigneeId; // Simplified from Entity for this assignment
    private Long taskDeadlineTime;
    private Priority priority;
}

