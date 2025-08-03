package com.assignment.workforcemgmt.dto;

import com.assignment.workforcemgmt.common.model.enums.ReferenceType;
import com.assignment.workforcemgmt.model.Task;
//import com.assignment.workforcemgmt.model.TaskActivityDto;
//import com.assignment.workforcemgmt.model.TaskCommentDto;
import com.assignment.workforcemgmt.model.enums.Priority;
import com.assignment.workforcemgmt.model.enums.TaskStatus;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

import java.util.List;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class TaskManagementDto {
    private Long id;
    private Long referenceId;
    private ReferenceType referenceType;
    private Task task;
    private String description;
    private TaskStatus status;
    private Long assigneeId;
    private Long taskDeadlineTime;
    private Priority priority;
//    private List<TaskActivityDto> activityHistory;
//    private List<TaskCommentDto> comments;

}
