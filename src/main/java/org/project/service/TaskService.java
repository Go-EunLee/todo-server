package org.project.service;

import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.project.constants.TaskStatus;
import org.project.model.Task;
import org.project.persist.TaskRepository;
import org.project.persist.entity.TaskEntity;
import org.project.web.vo.TaskRequest;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

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

    public List<Task> getAll(){
        return taskRepository.findAll().stream()
                .map(this::entityToObject)
                .collect(Collectors.toList());
    }

    public List<Task> getByDueDate(String dueDate){
        return taskRepository.findAllByDueDate(Date.valueOf(dueDate))
                .stream().map(this::entityToObject)
                .collect(Collectors.toList());
    }

    public List<Task> getByStatus(TaskStatus status){
        return taskRepository.findAllByStatus(status).stream()
                .map(this::entityToObject)
                .collect(Collectors.toList());
    }

    public Task getOne(Long id){
        var entity = this.getById(id);
        return entityToObject(entity);
    }

    public Task update(Long id, TaskRequest request){
        var exists = getById(id);

        exists.setTitle(Strings.isEmpty(request.getTitle()) ? exists.getTitle() : request.getTitle());
        exists.setDescription(Strings.isEmpty(request.getDescription()) ? exists.getDescription() : request.getDescription());
        exists.setDueDate(Objects.isNull(request.getDueDate()) ? exists.getDueDate() : Date.valueOf(request.getDueDate()));

        var updated = taskRepository.save(exists);
        return entityToObject(updated);

    }

    public Task updateStatus(Long id, TaskStatus status){
        var entity = getById(id);

        entity.setStatus(status);

        var saved = taskRepository.save(entity);

        return entityToObject(saved);
    }

    public boolean delete(Long id){
        try{
            taskRepository.deleteById(id);
        } catch (Exception e){
            log.error("an error occurred while deleting [{}]", e.toString());
            return false;
        }

        return true;
    }

    private TaskEntity getById(Long id){
        return taskRepository.findById(id)
                .orElseThrow(()-> new IllegalArgumentException(String.format("not exists task id [%d]", id)));
    }

    private Task entityToObject(TaskEntity entity){
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
