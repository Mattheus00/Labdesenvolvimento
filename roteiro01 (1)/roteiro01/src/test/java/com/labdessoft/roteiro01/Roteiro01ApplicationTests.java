package com.labdessoft.roteiro01;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.labdessoft.roteiro01.model.Task;
import com.labdessoft.roteiro01.model.TaskType;
import com.labdessoft.roteiro01.repository.TaskRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class TaskControllerIntegrationTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private TaskRepository taskRepository;

	@Autowired
	private ObjectMapper objectMapper;

	@BeforeEach
	public void setUp() {
		taskRepository.deleteAll();
	}

	@Test
	public void shouldCreateTask() throws Exception {
		Task task = new Task();
		task.setName("Test Task");
		task.setCompleted(false);
		task.setDueDate(LocalDate.now().plusDays(1));
		task.setType(TaskType.OTHER);

		mockMvc.perform(post("/tasks")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(task)))
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$.id").isNumber())
				.andExpect(jsonPath("$.name").value("Test Task"));
	}

	@Test
	public void shouldReturnBadRequestForInvalidTask() throws Exception {
		Task task = new Task();
		task.setName("Past Due Task");
		task.setCompleted(false);
		task.setDueDate(LocalDate.now().minusDays(1));
		task.setType(TaskType.DATA);

		mockMvc.perform(post("/tasks")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(task)))
				.andExpect(status().isBadRequest());
	}

	@Test
	public void shouldGetAllTasks() throws Exception {
		Task task1 = new Task();
		task1.setName("Task 1");
		task1.setCompleted(false);
		task1.setDueDate(LocalDate.now().plusDays(1));
		task1.setType(TaskType.OTHER);

		Task task2 = new Task();
		task2.setName("Task 2");
		task2.setCompleted(false);
		task2.setDueDate(LocalDate.now().plusDays(2));
		task2.setType(TaskType.DATA);

		taskRepository.save(task1);
		taskRepository.save(task2);

		mockMvc.perform(get("/tasks"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$[0].name").value("Task 1"))
				.andExpect(jsonPath("$[1].name").value("Task 2"));
	}

	@Test
	public void shouldMarkTaskAsCompleted() throws Exception {
		Task task = new Task();
		task.setName("Incomplete Task");
		task.setCompleted(false);
		task.setDueDate(LocalDate.now().plusDays(1));
		task.setType(TaskType.OTHER);

		task = taskRepository.save(task);

		mockMvc.perform(put("/tasks/{id}", task.getId()))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.completed").value(true));
	}

	@Test
	public void shouldDeleteTask() throws Exception {
		Task task = new Task();
		task.setName("Task to be deleted");
		task.setCompleted(false);
		task.setDueDate(LocalDate.now().plusDays(1));
		task.setType(TaskType.OTHER);

		task = taskRepository.save(task);

		mockMvc.perform(delete("/tasks/{id}", task.getId()))
				.andExpect(status().isNoContent());
	}
}
