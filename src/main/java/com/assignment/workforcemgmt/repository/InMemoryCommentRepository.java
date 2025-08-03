package com.assignment.workforcemgmt.repository;

import com.assignment.workforcemgmt.model.Comment;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class InMemoryCommentRepository implements CommentRepository {

    private final Map<Long, List<Comment>> store = new ConcurrentHashMap<>();
    private final AtomicLong counter = new AtomicLong(1);

    @Override
    public void save(Comment comment) {
        comment.setId(counter.getAndIncrement());
        store.computeIfAbsent(comment.getTaskId(), k -> Collections.synchronizedList(new ArrayList<>())).add(comment);
    }

    @Override
    public List<Comment> findByTaskId(Long taskId) {
        return store.getOrDefault(taskId, List.of())
                .stream()
                .sorted(Comparator.comparingLong(Comment::getTimestamp))
                .toList();
    }
}