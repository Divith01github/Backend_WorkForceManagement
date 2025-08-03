
package com.assignment.workforcemgmt.service.impl;

import com.assignment.workforcemgmt.common.exception.ResourceNotFoundException;
import com.assignment.workforcemgmt.dto.*;
import com.assignment.workforcemgmt.mapper.ITaskManagementMapper;
import com.assignment.workforcemgmt.model.*;
import com.assignment.workforcemgmt.model.enums.Priority;
import com.assignment.workforcemgmt.model.enums.TaskStatus;
import com.assignment.workforcemgmt.repository.ActivityLogRepository;
import com.assignment.workforcemgmt.repository.CommentRepository;
import com.assignment.workforcemgmt.repository.TaskRepository;
import com.assignment.workforcemgmt.service.TaskManagementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TaskManagementServiceImpl implements TaskManagementService {

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private ActivityLogRepository activityLogRepository;
    private final TaskRepository taskRepository;
    private final ITaskManagementMapper taskMapper;

    public TaskManagementServiceImpl(TaskRepository taskRepository, ITaskManagementMapper taskMapper) {
        this.taskRepository = taskRepository;
        this.taskMapper = taskMapper;
    }

    @Override
    public TaskManagementDto findTaskById(Long id) {
        TaskManagement task = taskRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found with id: " + id));
        return taskMapper.modelToDto(task);
    }

    @Override
    public List<TaskManagementDto> createTasks(TaskCreateRequest createRequest) {
        List<TaskManagement> createdTasks = new ArrayList<>();

        for (TaskCreateRequest.RequestItem item : createRequest.getRequests()) {
            TaskManagement newTask = new TaskManagement();
            newTask.setReferenceId(item.getReferenceId());
            newTask.setReferenceType(item.getReferenceType());
            newTask.setTask(item.getTask());
            newTask.setAssigneeId(item.getAssigneeId());
            newTask.setPriority(item.getPriority());
            newTask.setTaskDeadlineTime(item.getTaskDeadlineTime());
            newTask.setStatus(TaskStatus.ASSIGNED);
            newTask.setDescription("New task created.");

            TaskManagement savedTask = taskRepository.save(newTask);
            createdTasks.add(savedTask);

            // Log task creation
            ActivityLog log = new ActivityLog();
            log.setTaskId(savedTask.getId());
            log.setMessage("Task created with priority " + savedTask.getPriority() + " and assigned to user " + savedTask.getAssigneeId());
            log.setTimestamp(System.currentTimeMillis());
            activityLogRepository.save(log);
        }

        return taskMapper.modelListToDtoList(createdTasks);
    }

    @Override
    public List<TaskManagementDto> updateTasks(UpdateTaskRequest updateRequest) {
        List<TaskManagement> updatedTasks = new ArrayList<>();

        for (UpdateTaskRequest.RequestItem item : updateRequest.getRequests()) {
            TaskManagement task = taskRepository.findById(item.getTaskId())
                    .orElseThrow(() -> new ResourceNotFoundException("Task not found with id: " + item.getTaskId()));

            if (item.getTaskStatus() != null && item.getTaskStatus() != task.getStatus()) {
                task.setStatus(item.getTaskStatus());
                ActivityLog log = new ActivityLog();
                log.setTaskId(task.getId());
                log.setMessage("Status changed to " + item.getTaskStatus());
                log.setTimestamp(System.currentTimeMillis());
                activityLogRepository.save(log);
            }

            if (item.getDescription() != null && !item.getDescription().equals(task.getDescription())) {
                task.setDescription(item.getDescription());
                ActivityLog log = new ActivityLog();
                log.setTaskId(task.getId());
                log.setMessage("Description updated");
                log.setTimestamp(System.currentTimeMillis());
                activityLogRepository.save(log);
            }

            updatedTasks.add(taskRepository.save(task));
        }

        return taskMapper.modelListToDtoList(updatedTasks);
    }
//    @Override
//    public String assignByReference(AssignByReferenceRequest request) {
//        List<Task> applicableTasks = Task.getTasksByReferenceType(request.getReferenceType());
//        List<TaskManagement> existingTasks = taskRepository.findByReferenceIdAndReferenceType(request.getReferenceId(), request.getReferenceType());
//
//
//        for (Task taskType : applicableTasks) {
//            List<TaskManagement> tasksOfType = existingTasks.stream()
//                    .filter(t -> t.getTask() == taskType && t.getStatus() != TaskStatus.COMPLETED)
//                    .collect(Collectors.toList());
//
//
//            // BUG #1 is here. It should assign one and cancel the rest.
//            // Instead, it reassigns ALL of them.
//            if (!tasksOfType.isEmpty()) {
//                for (TaskManagement taskToUpdate : tasksOfType) {
//                    taskToUpdate.setAssigneeId(request.getAssigneeId());
//                    taskRepository.save(taskToUpdate);
//                }
//            } else {
//                // Create a new task if none exist
//                TaskManagement newTask = new TaskManagement();
//                newTask.setReferenceId(request.getReferenceId());
//                newTask.setReferenceType(request.getReferenceType());
//                newTask.setTask(taskType);
//                newTask.setAssigneeId(request.getAssigneeId());
//                newTask.setStatus(TaskStatus.ASSIGNED);
//                taskRepository.save(newTask);
//            }
//        }
//        return "Tasks assigned successfully for reference " + request.getReferenceId();
//    }


    @Override
    public String assignByReference(AssignByReferenceRequest request) {
        List<Task> applicableTasks = Task.getTasksByReferenceType(request.getReferenceType());
        List<TaskManagement> existingTasks = taskRepository.findByReferenceIdAndReferenceType(request.getReferenceId(), request.getReferenceType());

        for (Task taskType : applicableTasks) {
            List<TaskManagement> tasksOfType = existingTasks.stream()
                    .filter(t -> t.getTask() == taskType && t.getStatus() != TaskStatus.COMPLETED)
                    .collect(Collectors.toList());


            boolean taskAssigned = false;

            for (TaskManagement taskToUpdate : tasksOfType) {
                if (taskToUpdate.getAssigneeId().equals(request.getAssigneeId())) {
                    taskAssigned = true;
                } else {
                    taskToUpdate.setStatus(TaskStatus.CANCELLED);
                    taskRepository.save(taskToUpdate);

                    ActivityLog cancelLog = new ActivityLog();
                    cancelLog.setTaskId(taskToUpdate.getId());
                    cancelLog.setMessage("Task cancelled due to reassignment");
                    cancelLog.setTimestamp(System.currentTimeMillis());
                    activityLogRepository.save(cancelLog);
                }
            }

            if (!taskAssigned) {
                TaskManagement newTask = new TaskManagement();
                newTask.setReferenceId(request.getReferenceId());
                newTask.setReferenceType(request.getReferenceType());
                newTask.setTask(taskType);
                newTask.setAssigneeId(request.getAssigneeId());
                newTask.setStatus(TaskStatus.ASSIGNED);
                TaskManagement created = taskRepository.save(newTask);

                ActivityLog assignLog = new ActivityLog();
                assignLog.setTaskId(created.getId());
                assignLog.setMessage("Task assigned to user " + created.getAssigneeId());
                assignLog.setTimestamp(System.currentTimeMillis());
                activityLogRepository.save(assignLog);
            }
        }

        return "Tasks assigned successfully for reference " + request.getReferenceId();
    }

//    @Override
//    public List<TaskManagementDto> fetchTasksByDate(TaskFetchByDateRequest request) {
//        List<TaskManagement> tasks = taskRepository.findByAssigneeIdIn(request.getAssigneeIds());
//
//
//        // BUG #2 is here. It should filter out CANCELLED tasks but doesn't.
//        List<TaskManagement> filteredTasks = tasks.stream()
//                .filter(task -> {
//                    // This logic is incomplete for the assignment.
//                    // It should check against startDate and endDate.
//                    // For now, it just returns all tasks for the assignees.
//                    return true;
//                })
//                .collect(Collectors.toList());
//
//
//        return taskMapper.modelListToDtoList(filteredTasks);
//    }
//}

    @Override
    public List<TaskManagementDto> fetchTasksByDate(TaskFetchByDateRequest request) {
        List<TaskManagement> allTasks = taskRepository.findByAssigneeIdIn(request.getAssigneeIds());

        long startDate = request.getStartDate();
        long endDate = request.getEndDate();

        List<TaskManagement> filteredTasks = allTasks.stream()
                .filter(task -> {
                    TaskStatus status = task.getStatus();
                    boolean isActive = status == TaskStatus.ASSIGNED || status == TaskStatus.STARTED;
                    if (!isActive) return false;
                    long deadline = task.getTaskDeadlineTime();
                    return (deadline >= startDate && deadline <= endDate) || (deadline < startDate);
                })
                .collect(Collectors.toList());

        return taskMapper.modelListToDtoList(filteredTasks);
    }
}



