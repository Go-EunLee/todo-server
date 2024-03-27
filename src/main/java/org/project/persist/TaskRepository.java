package org.project.persist;

import org.project.constants.TaskStatus;
import org.project.persist.entity.TaskEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<TaskEntity, Long> {
    List<TaskEntity> findAllByDueDate(Date dueDate);
    List<TaskEntity> findAllByStatus(TaskStatus status);
}
