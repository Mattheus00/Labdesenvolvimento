package com.labdessoft.roteiro01.entity;

import jakarta.persistence.*;


import java.time.LocalDate;

@Entity
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Mudei para IDENTITY que é mais comum com o Auto Increment dos bancos de dados.
    private Long id;

    private String title;
    private String description;

    @Enumerated(EnumType.STRING)
    private TaskType type;

    private LocalDate dueDate;  // Usado para tarefas do tipo DATA
    private Integer dueInDays;  // Usado para tarefas do tipo PRAZO
    private boolean completed;

    @Enumerated(EnumType.STRING)
    private Priority priority;

    @Enumerated(EnumType.STRING)
    private Status status;

    // Construtores
    public Task() {
    }

    public Task(String title, String description, TaskType type, LocalDate dueDate, Integer dueInDays, Priority priority, boolean completed) {
        this.title = title;
        this.description = description;
        this.type = type;
        this.dueDate = dueDate;
        this.dueInDays = dueInDays;
        this.priority = priority;
        this.completed = completed;
        this.updateStatus();
    }

    // Métodos getter e setter
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public TaskType getType() {
        return type;
    }

    public void setType(TaskType type) {
        this.type = type;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    public Integer getDueInDays() {
        return dueInDays;
    }

    public void setDueInDays(Integer dueInDays) {
        this.dueInDays = dueInDays;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
        updateStatus(); // Atualiza o status sempre que a conclusão é alterada
    }

    public Priority getPriority() {
        return priority;
    }

    public void setPriority(Priority priority) {
        this.priority = priority;
    }

    public Status getStatus() {
        return status;
    }

    // O setStatus é intencionalmente privado para forçar o uso do método updateStatus
    public void setStatus(Status status) {
        this.status = status;
    }

    public void updateStatus() {
        if (completed) {
            status = Status.CONCLUIDA;
        } else {
            switch (type) {
                case DATA:
                case PRAZO:
                    if (dueDate != null && LocalDate.now().isAfter(dueDate)) {
                        status = Status.ATRASO;
                    } else {
                        status = Status.PREVISTA;
                    }
                    break;
                case LIVRE:
                    status = Status.PREVISTA;
                    break;
            }
        }
    }
}


