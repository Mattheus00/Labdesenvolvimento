package com.labdessoft.roteiro01.repository;

import com.labdessoft.roteiro01.entity.Task;
import com.labdessoft.roteiro01.entity.TaskType;
import com.labdessoft.roteiro01.entity.Status;
import com.labdessoft.roteiro01.entity.Priority;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
    // Buscar tarefas por tipo
    List<Task> findByType(TaskType type);

    // Buscar tarefas por status
    List<Task> findByStatus(Status status);

    // Buscar tarefas por prioridade
    List<Task> findByPriority(Priority priority);

    // Buscar tarefas que estão atrasadas
    List<Task> findByStatusAndDueDateBefore(Status status, LocalDate date);

    // Buscar tarefas concluídas
    List<Task> findByCompletedTrue();
}
