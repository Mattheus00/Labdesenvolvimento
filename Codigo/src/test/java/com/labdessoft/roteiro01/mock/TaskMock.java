package com.labdessoft.roteiro01.mock;

import com.labdessoft.roteiro01.entity.Priority;
import com.labdessoft.roteiro01.entity.Status;
import com.labdessoft.roteiro01.entity.Task;
import com.labdessoft.roteiro01.entity.TaskType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import java.time.LocalDate;
import java.util.Arrays;

public class TaskMock {

    public static Page<Task> createTasks() {
        Task task1 = new Task("Task 1", "Description 1", TaskType.DATA, LocalDate.now().plusDays(5), null, Priority.ALTA, false);
        Task task2 = new Task("Task 2", "Description 2", TaskType.PRAZO, null, 3, Priority.MEDIA, false);
        return new PageImpl<>(Arrays.asList(task1, task2));
    }
}
