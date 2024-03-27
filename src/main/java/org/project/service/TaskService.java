package org.project.service;

import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.project.constants.TaskStatus;
import org.project.model.Task;
import org.project.persist.TaskRepository;
import org.project.persist.entity.TaskEntity;
import org.project.web.vo.TaskRequest;
import org.springframework.stereotype.Service;

import java.sql.Date;

@Slf4j
@Service
@RequiredArgsConstructor
public class TaskService {

    private final TaskRepository taskRepository;

    public Task add(TaskRequest request){
        var e = TaskEntity.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .dueDate(Date.valueOf(request.getDueDate()))
                .status(TaskStatus.TODO)
                .build();
        var saved = taskRepository.save(e);
        return entityToObject(saved);
    }

    public Task entityToObject(TaskEntity entity){
        return Task.builder()
                .id(entity.getId())
                .title(entity.getTitle())
                .description(entity.getDescription())
                .status(entity.getStatus())
                .dueDate(entity.getDueDate().toString())
                .createdAt(entity.getCreatedAt().toLocalDateTime())
                .updatedAt(entity.getUpdatedAt().toLocalDateTime())
                .build();
    }
}
