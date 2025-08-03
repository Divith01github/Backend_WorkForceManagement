package com.assignment.workforcemgmt.repository;




import com.assignment.workforcemgmt.model.ActivityLog;

import java.util.List;

public interface ActivityLogRepository {
    void save(ActivityLog log);
    List<ActivityLog> findByTaskId(Long taskId);
}
