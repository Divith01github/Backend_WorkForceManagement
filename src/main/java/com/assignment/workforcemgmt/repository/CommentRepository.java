package com.assignment.workforcemgmt.repository;
import com.assignment.workforcemgmt.model.Comment;

import java.util.List;

public interface CommentRepository {
    void save(Comment comment);
    List<Comment> findByTaskId(Long taskId);
}