package com.assignment.workforcemgmt.service.impl;

import com.assignment.workforcemgmt.common.exception.ResourceNotFoundException;
import com.assignment.workforcemgmt.dto.*;
import com.assignment.workforcemgmt.mapper.ITaskManagementMapper;
import com.assignment.workforcemgmt.model.ActivityLog;
import com.assignment.workforcemgmt.model.Comment;
import com.assignment.workforcemgmt.model.TaskManagement;
import com.assignment.workforcemgmt.model.enums.Priority;
import com.assignment.workforcemgmt.repository.ActivityLogRepository;
import com.assignment.workforcemgmt.repository.CommentRepository;
import com.assignment.workforcemgmt.repository.TaskRepository;
import com.assignment.workforcemgmt.service.NewTaskManagementService;
import com.assignment.workforcemgmt.service.TaskManagementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class NewTaskManagementImpl implements NewTaskManagementService {
    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private ActivityLogRepository activityLogRepository;

    private final TaskRepository newTaskRepository;
    private final ITaskManagementMapper newTaskMapper;
//    @Autowired
//    private CommentRepository commentRepository;
//
//    @Autowired
//    private ActivityLogRepository activityLogRepository;
//    private final TaskRepository taskRepository;
//    private final ITaskManagementMapper taskMapper;
//
//    public TaskManagementServiceImpl(TaskRepository taskRepository, ITaskManagementMapper taskMapper) {
//        this.taskRepository = taskRepository;
//        this.taskMapper = taskMapper;
//    }


    public NewTaskManagementImpl(TaskRepository newTaskRepository, ITaskManagementMapper newTaskMapper) {
        this.newTaskRepository = newTaskRepository;
        this.newTaskMapper = newTaskMapper;
    }

    @Override
    public TaskManagementDto updateTaskPriority(UpdatePriorityRequest request) {
        TaskManagement task = newTaskRepository.findById(request.getTaskId())
                .orElseThrow(() -> new ResourceNotFoundException("Task not found with id: " + request.getTaskId()));

        Priority oldPriority = task.getPriority();
        task.setPriority(request.getNewPriority());
        newTaskRepository.save(task);

        ActivityLog log = new ActivityLog();
        log.setTaskId(task.getId());
        log.setMessage("Priority changed from " + oldPriority + " to " + request.getNewPriority());
        log.setTimestamp(System.currentTimeMillis());
        activityLogRepository.save(log);

        return newTaskMapper.modelToDto(task);
    }

    @Override
    public List<TaskManagementDto> getTasksByPriority(Priority priority) {
        List<TaskManagement> tasks = newTaskRepository.findAll().stream()
                .filter(t -> t.getPriority() == priority)
                .collect(Collectors.toList());
        return newTaskMapper.modelListToDtoList(tasks);
    }

    @Override
    public TaskDetailDto getTaskDetails(Long taskId) {
        TaskManagement task = newTaskRepository.findById(taskId).get();
        if (task == null) {
            throw new ResourceNotFoundException("Task with ID " + taskId + " not found");
        }

        List<CommentDto> comments = commentRepository.findByTaskId(taskId).stream()
                .map(comment -> {
                    CommentDto dto = new CommentDto();
                    dto.setCommenterId(comment.getCommenterId());
                    dto.setCommentText(comment.getCommentText());
                    dto.setTimestamp(comment.getTimestamp());
                    return dto;
                }).toList();

        List<ActivityLogDto> logs = activityLogRepository.findByTaskId(taskId).stream()
                .map(log -> {
                    ActivityLogDto dto = new ActivityLogDto();
                    dto.setMessage(log.getMessage());
                    dto.setTimestamp(log.getTimestamp());
                    return dto;
                }).toList();

        TaskDetailDto detail = new TaskDetailDto();
        detail.setTask(newTaskMapper.modelToDto(task));

        detail.setComments(comments);
        detail.setActivityLogs(logs);

        return detail;
    }

    @Override
    public void addComment(Long taskId, CommentDto commentDto) {
        TaskManagement task = newTaskRepository.findById(taskId).get();
        if (task == null) {
            throw new ResourceNotFoundException("Task with ID " + taskId + " not found");
        }

        Comment comment = new Comment();
        comment.setTaskId(taskId);
        comment.setCommenterId(commentDto.getCommenterId());
        comment.setCommentText(commentDto.getCommentText());
        comment.setTimestamp(System.currentTimeMillis());

        commentRepository.save(comment);

        ActivityLog log = new ActivityLog();
        log.setTaskId(taskId);
        log.setMessage("User " + commentDto.getCommenterId() + " commented: " + commentDto.getCommentText());
        log.setTimestamp(System.currentTimeMillis());
        activityLogRepository.save(log);
    }
}
