package org.example.app.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long> {
    @Query(value = "SELECT id " +
            "FROM tasks " +
            "WHERE status = :status " +
            "LIMIT 10 " +
            "FOR UPDATE SKIP LOCKED", nativeQuery = true)
    List<Long> findPendingTasksWithLock(@Param("status") Status status);
}
