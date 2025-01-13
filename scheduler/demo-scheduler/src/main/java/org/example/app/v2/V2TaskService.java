package org.example.app.v2;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.app.persistence.Status;
import org.example.app.persistence.TaskRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class V2TaskService {
    private final TaskRepository taskRepository;

    public List<Long> lockTask() {
        return taskRepository.findPendingTasksWithLock(Status.PENDING);
    }
}
