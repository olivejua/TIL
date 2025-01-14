package org.example.app.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long> {
    @Query(value = "SELECT id " +
            "FROM tasks " +
            "WHERE status = :status " +
            "LIMIT 10 " +
            "FOR UPDATE SKIP LOCKED", nativeQuery = true)
    List<Long> findPendingTasksWithLock(@Param("status") Status status);

    @Query(value = "SELECT id " +
            "FROM tasks " +
            "WHERE status = :status " +
            "LIMIT 10", nativeQuery = true)
    List<Long> findPendingTasks(@Param("status") Status status);

    @Modifying
    @Query(value = "UPDATE Task t " +
            "SET t.status = :status, t.updatedAt = :updatedAt " +
            "WHERE id IN (:ids)")
    void updateStatusByIds(@Param("ids") List<Long> ids, @Param("status") Status status, @Param("updatedAt") LocalDateTime updatedAt);
}
