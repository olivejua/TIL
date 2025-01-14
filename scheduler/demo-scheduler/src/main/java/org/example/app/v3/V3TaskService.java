package org.example.app.v3;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.app.persistence.Status;
import org.example.app.persistence.TaskRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class V3TaskService {
    private final TaskRepository taskRepository;

    @Transactional
    public List<Long> getTasks() {
        List<Long> taskIds = taskRepository.findPendingTasks(Status.PENDING);
        taskRepository.updateStatusByIds(taskIds, Status.PROCESSING, LocalDateTime.now());

        return taskIds;
    }
}

