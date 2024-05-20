package com.labdessoft.roteiro01.controller;

import com.labdessoft.roteiro01.model.Task;
import com.labdessoft.roteiro01.model.TaskType;
import com.labdessoft.roteiro01.repository.TaskRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class TaskControllerTest {

    @InjectMocks
    private TaskController taskController;

    @Mock
    private TaskRepository taskRepository;

    public TaskControllerTest() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetAllTasks() {
        Task task1 = new Task();
        task1.setId(1L);
        task1.setName("Task 1");
        task1.setCompleted(false);
        task1.setDueDate(LocalDate.now().plusDays(1));
        task1.setType(TaskType.OTHER);

        Task task2 = new Task();
        task2.setId(2L);
        task2.setName("Task 2");
        task2.setCompleted(false);
        task2.setDueDate(LocalDate.now().plusDays(2));
        task2.setType(TaskType.DATA);

        when(taskRepository.findAll()).thenReturn(Arrays.asList(task1, task2));

        ResponseEntity<List<Task>> response = taskController.getAllTasks();
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(2, response.getBody().size());
    }

    @Test
    public void testCreateTask() {
        Task task = new Task();
        task.setId(1L);
        task.setName("New Task");
        task.setCompleted(false);
        task.setDueDate(LocalDate.now().plusDays(1));
        task.setType(TaskType.OTHER);

        when(taskRepository.save(any(Task.class))).thenReturn(task);

        ResponseEntity<Task> response = taskController.createTask(task);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals("New Task", response.getBody().getName());
    }

    @Test
    public void testCreateTaskWithPastDueDate() {
        Task task = new Task();
        task.setId(1L);
        task.setName("Past Due Task");
        task.setCompleted(false);
        task.setDueDate(LocalDate.now().minusDays(1));
        task.setType(TaskType.DATA);

        ResponseEntity<Task> response = taskController.createTask(task);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    public void testMarkTaskAsCompleted() {
        Task task = new Task();
        task.setId(1L);
        task.setName("Incomplete Task");
        task.setCompleted(false);
        task.setDueDate(LocalDate.now().plusDays(1));
        task.setType(TaskType.OTHER);

        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));
        when(taskRepository.save(task)).thenReturn(task);

        ResponseEntity<Task> response = taskController.markTaskAsCompleted(1L);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(true, response.getBody().isCompleted());
    }

    @Test
    public void testMarkTaskAsCompletedNotFound() {
        when(taskRepository.findById(1L)).thenReturn(Optional.empty());

        ResponseEntity<Task> response = taskController.markTaskAsCompleted(1L);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void testDeleteTask() {
        doNothing().when(taskRepository).deleteById(1L);

        ResponseEntity<Void> response = taskController.deleteTask(1L);
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }
}
