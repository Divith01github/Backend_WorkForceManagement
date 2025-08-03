package com.assignment.workforcemgmt.repository;

import com.assignment.workforcemgmt.model.ActivityLog;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class InMemoryActivityLogRepository implements ActivityLogRepository {

    private final Map<Long, List<ActivityLog>> store = new ConcurrentHashMap<>();
    private final AtomicLong counter = new AtomicLong(1);

    @Override
    public void save(ActivityLog log) {
        log.setId(counter.getAndIncrement());
        store.computeIfAbsent(log.getTaskId(), k -> Collections.synchronizedList(new ArrayList<>())).add(log);
    }

    @Override
    public List<ActivityLog> findByTaskId(Long taskId) {
        return store.getOrDefault(taskId, List.of())
                .stream()
                .sorted(Comparator.comparingLong(ActivityLog::getTimestamp))
                .toList();
    }
}