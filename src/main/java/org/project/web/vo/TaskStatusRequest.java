package org.project.web.vo;

import lombok.Getter;
import lombok.ToString;
import org.project.constants.TaskStatus;

@Getter
@ToString
public class TaskStatusRequest {
    private TaskStatus status;
}
