package com.assignment.workforcemgmt.dto;


import com.assignment.workforcemgmt.model.enums.Priority;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class UpdatePriorityRequest {
    private Long taskId;
    private Priority newPriority;
}