package com.assignment.workforcemgmt.service;

import com.assignment.workforcemgmt.common.exception.ResourceNotFoundException;
import com.assignment.workforcemgmt.dto.*;
import com.assignment.workforcemgmt.model.TaskManagement;
import com.assignment.workforcemgmt.model.enums.Priority;

import java.util.List;

public interface TaskManagementService {
    List<TaskManagementDto> createTasks(TaskCreateRequest request);
    List<TaskManagementDto> updateTasks(UpdateTaskRequest request);
    String assignByReference(AssignByReferenceRequest request);
    List<TaskManagementDto> fetchTasksByDate(TaskFetchByDateRequest request);
    TaskManagementDto findTaskById(Long id);


}

