package com.assignment.workforcemgmt.controller;

import com.assignment.workforcemgmt.common.model.response.Response;
import com.assignment.workforcemgmt.dto.*;
import com.assignment.workforcemgmt.model.enums.Priority;
import com.assignment.workforcemgmt.service.NewTaskManagementService;
import com.assignment.workforcemgmt.service.TaskManagementService;
import org.springframework.web.bind.annotation.*;
import com.assignment.workforcemgmt.common.model.response.ResponseStatus;
//import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/task-mgmt")
public class TaskManagementController {


    private final TaskManagementService taskManagementService;
    private final NewTaskManagementService newTaskManagementService;


    public TaskManagementController(TaskManagementService taskManagementService, NewTaskManagementService newTaskManagementService) {
        this.taskManagementService = taskManagementService;
        this.newTaskManagementService = newTaskManagementService;
    }


    @GetMapping("/{id}")
    public Response<TaskManagementDto> getTaskById(@PathVariable Long id) {
        return new Response<>(taskManagementService.findTaskById(id));
    }



    @PostMapping("/create")
    public Response<List<TaskManagementDto>> createTasks(@RequestBody TaskCreateRequest request) {
        return new Response<>(taskManagementService.createTasks(request));
    }


    @PostMapping("/update")
    public Response<List<TaskManagementDto>> updateTasks(@RequestBody UpdateTaskRequest request) {
        return new Response<>(taskManagementService.updateTasks(request));
    }


    @PostMapping("/assign-by-ref")
    public Response<String> assignByReference(@RequestBody AssignByReferenceRequest request) {
        return new Response<>(taskManagementService.assignByReference(request));
    }


    @PostMapping("/fetch-by-date/v2")
    public Response<List<TaskManagementDto>> fetchByDate(@RequestBody TaskFetchByDateRequest request) {
        return new Response<>(taskManagementService.fetchTasksByDate(request));
    }
    @PatchMapping("/update-priority")
    public Response<TaskManagementDto> updateTaskPriority(@RequestBody UpdatePriorityRequest request) {
        return new Response<>(newTaskManagementService.updateTaskPriority(request));
    }

    @GetMapping("/priority/{priority}")
    public Response<List<TaskManagementDto>> getTasksByPriority(@PathVariable Priority priority) {
        return new Response<>(newTaskManagementService.getTasksByPriority(priority));
    }

    @GetMapping("/tasks/{taskId}/details")
    public Response<TaskDetailDto> getTaskDetails(@PathVariable Long taskId) {
        TaskDetailDto dto = newTaskManagementService.getTaskDetails(taskId);
        return new Response<>(dto);
    }

    @PostMapping("/tasks/{taskId}/comments")
    public Response<Void> addComment(@PathVariable Long taskId, @RequestBody CommentDto commentDto) {
        newTaskManagementService.addComment(taskId, commentDto);
        ResponseStatus s= new ResponseStatus(200, "Comment added successfully");
        return new Response<>(null, null,s);
    }



}
