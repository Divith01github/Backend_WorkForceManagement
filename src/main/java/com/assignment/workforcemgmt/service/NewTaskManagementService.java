package com.assignment.workforcemgmt.service;

import com.assignment.workforcemgmt.dto.CommentDto;
import com.assignment.workforcemgmt.dto.TaskDetailDto;
import com.assignment.workforcemgmt.dto.TaskManagementDto;
import com.assignment.workforcemgmt.dto.UpdatePriorityRequest;
import com.assignment.workforcemgmt.model.enums.Priority;

import java.util.List;

public interface NewTaskManagementService {

    TaskDetailDto getTaskDetails(Long taskId);
    void addComment(Long taskId, CommentDto commentDto);

    TaskManagementDto updateTaskPriority(UpdatePriorityRequest request);
    List<TaskManagementDto> getTasksByPriority(Priority priority);
}
