package org.project.web;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.project.constants.TaskStatus;
import org.project.model.Task;
import org.project.service.TaskService;
import org.project.web.vo.ResultResponse;
import org.project.web.vo.TaskRequest;
import org.project.web.vo.TaskStatusRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Slf4j
@Controller
@RequestMapping("/tasks")
@RequiredArgsConstructor
public class TaskController {
    private final TaskService taskService;

    @PostMapping
    public ResponseEntity<Task> createTask(@RequestBody TaskRequest request){
        var result = taskService.add(request);
        return ResponseEntity.ok(result);
    }

    // 특정 마감일에 해당하는 할일 목록을 반환
    @GetMapping
    public ResponseEntity<List<Task>> getTask(Optional<String> dueDate){
        List<Task> result;

        if (dueDate.isPresent()) {
            result = taskService.getByDueDate(dueDate.get());
        } else{
            result = taskService.getAll();
        }
        return ResponseEntity.ok(result);
    }

    // 특정 ID에 해당하는 할일을 조회
    @GetMapping("/{id}")
    public ResponseEntity<Task> fetchOneTask(@PathVariable Long id){
        var result = taskService.getOne(id);
        return ResponseEntity.ok(result);
    }

    // 특정 상태에 해당하는 할일 목록을 반환
    @GetMapping("/status/{status}")
    public ResponseEntity<List<Task>> getByStatus(@PathVariable TaskStatus status){
        var result = taskService.getByStatus(status);
        return ResponseEntity.ok(result);
    }

    // 특정 ID에 해당하는 할일을 수정
    @PutMapping("/{id}")
    public ResponseEntity<Task> updateTask(
            @PathVariable Long id,
            @RequestBody TaskRequest task
    ){
        var result = taskService.update(id, task);
        return ResponseEntity.ok(result);
    }

    // 특정 ID에 해당하는 할일의 상태를 수정
    @PatchMapping("/{id}/status")
    public ResponseEntity<Task> updateTaskStatus(
            @PathVariable Long id,
            @RequestBody TaskStatusRequest req
    ){
        var result = taskService.updateStatus(id, req.getStatus());
        return ResponseEntity.ok(result);
    }

    // 특정 ID에 해당하는 할일을 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<ResultResponse> deleteTask(@PathVariable Long id){
        var result = taskService.delete(id);
        return ResponseEntity.ok(new ResultResponse(result));
    }

    @GetMapping("/status")
    public ResponseEntity<TaskStatus[]> getAllStatus(){
        var status = TaskStatus.values();
        return ResponseEntity.ok(status);
    }
}
