package org.example.app.v2;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.app.common.TaskService;
import org.example.app.persistence.Status;
import org.example.app.persistence.TaskRepository;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
public class V2TaskService implements TaskService {
    private final TaskRepository taskRepository;

    public List<Long> getTasks() {
        return taskRepository.findPendingTasksWithLock(Status.PENDING);
    }
}
