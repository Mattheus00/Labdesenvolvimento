package com.labdessoft.roteiro01.unit.service;

import com.labdessoft.roteiro01.entity.Task;
import com.labdessoft.roteiro01.mock.TaskMock;
import com.labdessoft.roteiro01.repository.TaskRepository;
import com.labdessoft.roteiro01.service.TaskService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(MockitoExtension.class)
public class TaskServiceTest {

    @Mock
    TaskRepository tasksRepository;

    private TaskService taskService;

    @BeforeEach
    public void setup() {
        taskService = new TaskService(tasksRepository);
        Pageable pageable = PageRequest.of(0, 5, Sort.by(
                Sort.Order.asc("title"),
                Sort.Order.desc("id")));

        Mockito.lenient().when(tasksRepository.findAll(pageable)).thenReturn(TaskMock.createTasks());
    }

    @Test
    @DisplayName("Should return all tasks")
    public void should_list_all_tasks_repository() {
        Pageable pageable = PageRequest.of(0, 5, Sort.by(
                Sort.Order.asc("title"),
                Sort.Order.desc("id")));

        Page<Task> tasks = taskService.listAll(pageable);

        assertEquals(1, tasks.getTotalPages());
        assertEquals(2, tasks.getNumberOfElements());
        assertNotNull(tasks);
    }
}
