package com.assignment.workforcemgmt.dto;

import com.assignment.workforcemgmt.common.model.enums.ReferenceType;
import com.assignment.workforcemgmt.model.Task;
import com.assignment.workforcemgmt.model.enums.Priority;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

import java.util.List;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class TaskCreateRequest {
    private List<RequestItem> requests;
    @Data
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class RequestItem {
        private Long referenceId;
        private ReferenceType referenceType;
        private Task task;
        private Long assigneeId;
        private Priority priority;
        private Long taskDeadlineTime;
    }
}

