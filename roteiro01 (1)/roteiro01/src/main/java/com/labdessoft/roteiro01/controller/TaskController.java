package com.labdessoft.roteiro01.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TaskController {

    @Autowired
    private TaskRepository taskRepository;

    // Endpoint para obter todas as tarefas
    @GetMapping
    public ResponseEntity<List<Task>> getAllTasks() {
        List<Task> tasks = taskRepository.findAll();
        return new ResponseEntity<>(tasks, HttpStatus.OK);
    }

    // Endpoint para criar uma nova tarefa
    @PostMapping
    public ResponseEntity<Task> createTask(@RequestBody Task task) {
        Task savedTask = taskRepository.save(task);
        return new ResponseEntity<>(savedTask, HttpStatus.CREATED);
    }

    // Endpoint para marcar uma tarefa como concluída
    @PutMapping("/{id}")
    public ResponseEntity<Task> markTaskAsCompleted(@PathVariable Long id) {
        Task task = taskRepository.findById(id).orElse(null);
        if (task == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        task.setCompleted(true);
        Task updatedTask = taskRepository.save(task);
        return new ResponseEntity<>(updatedTask, HttpStatus.OK);
    }

    // Endpoint para deletar uma tarefa
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long id) {
        taskRepository.deleteById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}