package com.labdessoft.roteiro01.unit.repository;

import com.labdessoft.roteiro01.entity.*;
import com.labdessoft.roteiro01.repository.TaskRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
@ExtendWith(MockitoExtension.class)
public class TaskRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private TaskRepository taskRepository;

    @BeforeEach
    public void setup() {
        Task task1 = new Task("Task 1", "Description 1", TaskType.DATA, LocalDate.now().plusDays(5), null, Priority.ALTA, false);
        task1.setStatus(Status.PREVISTA);
        Task task2 = new Task("Task 2", "Description 2", TaskType.PRAZO, null, 3, Priority.MEDIA, false);
        task2.setStatus(Status.PREVISTA);
        Task task3 = new Task("Task 3", "Description 3", TaskType.DATA, LocalDate.now().minusDays(1), null, Priority.BAIXA, true);
        task3.setStatus(Status.CONCLUIDA);

        entityManager.persist(task1);
        entityManager.persist(task2);
        entityManager.persist(task3);
        entityManager.flush();
    }

    @Test
    @DisplayName("Should find tasks by type")
    public void should_find_by_type() {
        List<Task> tasks = taskRepository.findByType(TaskType.DATA);
        assertFalse(tasks.isEmpty());
        assertEquals(2, tasks.size());
    }

    @Test
    @DisplayName("Should find tasks by status")
    public void should_find_by_status() {
        List<Task> tasks = taskRepository.findByStatus(Status.PREVISTA);
        assertFalse(tasks.isEmpty());
        assertEquals(2, tasks.size());
    }

    @Test
    @DisplayName("Should find tasks by priority")
    public void should_find_by_priority() {
        List<Task> tasks = taskRepository.findByPriority(Priority.ALTA);
        assertFalse(tasks.isEmpty());
        assertEquals(1, tasks.size());
    }

    @Test
    @DisplayName("Should find tasks that are overdue")
    public void should_find_overdue_tasks() {
        List<Task> tasks = taskRepository.findByStatusAndDueDateBefore(Status.PREVISTA, LocalDate.now());
        assertTrue(tasks.isEmpty());
    }

    @Test
    @DisplayName("Should find completed tasks")
    public void should_find_completed_tasks() {
        List<Task> tasks = taskRepository.findByCompletedTrue();
        assertFalse(tasks.isEmpty());
        assertEquals(1, tasks.size());
    }
}
