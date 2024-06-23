package com.labdessoft.roteiro01.service;

import com.labdessoft.roteiro01.entity.Status;
import com.labdessoft.roteiro01.entity.Task;
import com.labdessoft.roteiro01.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class TaskService {

    private final TaskRepository taskRepository;

    @Autowired
    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    public List<Task> findAllTasks() {
        return taskRepository.findAll();
    }

    public Page<Task> listAll(Pageable pageable) {
        return taskRepository.findAll(pageable);
    }

    public Optional<Task> findTaskById(Long id) {
        return taskRepository.findById(id);
    }

    public Task createTask(Task task) {
        validateTask(task); // Validação antes de criar uma tarefa
        updateStatus(task); // Atualiza o status baseado no tipo e data
        return taskRepository.save(task);
    }

    public Task updateTask(Long id, Task taskDetails) {
        Task existingTask = taskRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Task not found with id: " + id));

        existingTask.setTitle(taskDetails.getTitle());
        existingTask.setDescription(taskDetails.getDescription());
        existingTask.setDueDate(taskDetails.getDueDate());
        existingTask.setCompleted(taskDetails.isCompleted());
        validateTask(existingTask); // Validação antes de atualizar
        updateStatus(existingTask); // Atualiza o status conforme necessário
        return taskRepository.save(existingTask);
    }

    public void deleteTask(Long id) {
        taskRepository.deleteById(id);
    }

    private void validateTask(Task task) {
        if (task.getDueDate() != null && task.getDueDate().isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("Due date must be today or in the future.");
        }
    }

    private void updateStatus(Task task) {
        // Lógica para atualizar o status da tarefa, baseado em seu tipo e se está concluída
        if (task.isCompleted()) {
            task.setStatus(Status.CONCLUIDA);
        } else {
            if (task.getDueDate() != null && task.getDueDate().isBefore(LocalDate.now())) {
                task.setStatus(Status.ATRASO);
            } else {
                task.setStatus(Status.PREVISTA);
            }
        }
    }
}
