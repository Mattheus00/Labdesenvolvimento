package com.labdessoft.roteiro01.unit.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.labdessoft.roteiro01.controller.TaskController;
import com.labdessoft.roteiro01.entity.Priority;
import com.labdessoft.roteiro01.entity.Status;
import com.labdessoft.roteiro01.entity.Task;
import com.labdessoft.roteiro01.entity.TaskType;
import com.labdessoft.roteiro01.service.TaskService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest(TaskController.class)
public class TaskControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TaskService taskService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("Should create a task")
    public void should_create_task() throws Exception {
        Task task = new Task("Task 1", "Description 1", TaskType.DATA, LocalDate.now().plusDays(5), null, Priority.ALTA, false);
        Mockito.when(taskService.createTask(any(Task.class))).thenReturn(task);

        mockMvc.perform(post("/api/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(task)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title").value("Task 1"));
    }

    @Test
    @DisplayName("Should return all tasks")
    public void should_return_all_tasks() throws Exception {
        mockMvc.perform(get("/api/tasks")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Should return a task by id")
    public void should_return_task_by_id() throws Exception {
        Task task = new Task("Task 1", "Description 1", TaskType.DATA, LocalDate.now().plusDays(5), null, Priority.ALTA, false);
        Mockito.when(taskService.findTaskById(eq(1L))).thenReturn(Optional.of(task));

        mockMvc.perform(get("/api/tasks/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Task 1"));
    }

    @Test
    @DisplayName("Should update a task")
    public void should_update_task() throws Exception {
        Task task = new Task("Updated Task", "Updated Description", TaskType.PRAZO, LocalDate.now().plusDays(10), 7, Priority.BAIXA, false);
        Mockito.when(taskService.updateTask(eq(1L), any(Task.class))).thenReturn(task);

        mockMvc.perform(put("/api/tasks/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(task)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Updated Task"));
    }

    @Test
    @DisplayName("Should delete a task")
    public void should_delete_task() throws Exception {
        Mockito.doNothing().when(taskService).deleteTask(eq(1L));

        mockMvc.perform(delete("/api/tasks/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("Should mark a task as completed")
    public void should_mark_task_as_completed() throws Exception {
        Task task = new Task("Task 1", "Description 1", TaskType.DATA, LocalDate.now().plusDays(5), null, Priority.ALTA, false);
        Task completedTask = new Task("Task 1", "Description 1", TaskType.DATA, LocalDate.now().plusDays(5), null, Priority.ALTA, true);
        completedTask.setStatus(Status.CONCLUIDA);

        Mockito.when(taskService.findTaskById(eq(1L))).thenReturn(Optional.of(task));
        Mockito.when(taskService.updateTask(eq(1L), any(Task.class))).thenReturn(completedTask);

        mockMvc.perform(patch("/api/tasks/1/complete")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.completed").value(true))
                .andExpect(jsonPath("$.status").value(Status.CONCLUIDA.toString()));
    }
}
