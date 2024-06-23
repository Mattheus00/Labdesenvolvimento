package com.labdessoft.roteiro01.unit.entity;

import com.labdessoft.roteiro01.entity.Priority;
import com.labdessoft.roteiro01.entity.Status;
import com.labdessoft.roteiro01.entity.Task;
import com.labdessoft.roteiro01.entity.TaskType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

public class TaskTest {

    @Test
    @DisplayName("Should create a Task entity")
    public void should_create_task() {
        Task task = new Task("Task Title", "Task Description", TaskType.DATA, LocalDate.now().plusDays(5), null, Priority.ALTA, false);

        assertNotNull(task);
        assertEquals("Task Title", task.getTitle());
        assertEquals("Task Description", task.getDescription());
        assertEquals(TaskType.DATA, task.getType());
        assertEquals(LocalDate.now().plusDays(5), task.getDueDate());
        assertEquals(Priority.ALTA, task.getPriority());
        assertFalse(task.isCompleted());
    }

    @Test
    @DisplayName("Should update task status to CONCLUIDA when completed")
    public void should_update_status_to_concluida_when_completed() {
        Task task = new Task("Task Title", "Task Description", TaskType.DATA, LocalDate.now().plusDays(5), null, Priority.ALTA, false);
        task.setCompleted(true);
        task.updateStatus();

        assertEquals(Status.CONCLUIDA, task.getStatus());
    }

    @Test
    @DisplayName("Should update task status to ATRASO when due date is past and not completed")
    public void should_update_status_to_atraso_when_due_date_is_past_and_not_completed() {
        Task task = new Task("Task Title", "Task Description", TaskType.DATA, LocalDate.now().minusDays(1), null, Priority.ALTA, false);
        task.updateStatus();

        assertEquals(Status.ATRASO, task.getStatus());
    }

    @Test
    @DisplayName("Should update task status to PREVISTA when due date is in future and not completed")
    public void should_update_status_to_prevista_when_due_date_is_in_future_and_not_completed() {
        Task task = new Task("Task Title", "Task Description", TaskType.DATA, LocalDate.now().plusDays(5), null, Priority.ALTA, false);
        task.updateStatus();

        assertEquals(Status.PREVISTA, task.getStatus());
    }
}
