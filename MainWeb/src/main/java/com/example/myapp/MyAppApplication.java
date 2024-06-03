package com.example.myapp;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.List;
import java.util.Optional;

@SpringBootApplication
public class MyAppApplication {

    public static void main(String[] args) {
        SpringApplication.run(MyAppApplication.class, args);
    }

    @Bean
    public CommandLineRunner demo(TaskRepository repository) {
        return (args) -> {
            // Добавление некоторых задач в базу данных для примера
            repository.save(new Task("Task 1", "Description 1"));
            repository.save(new Task("Task 2", "Description 2"));
        };
    }
}

@Controller
class TaskController {

    @Autowired
    private TaskRepository taskRepository;

    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("tasks", taskRepository.findAll());
        return "index";
    }

    @GetMapping("/tasks/new")
    public String newTaskForm(Model model) {
        model.addAttribute("task", new Task());
        return "task_form";
    }

    @PostMapping("/tasks")
    public String createTask(@ModelAttribute Task task) {
        taskRepository.save(task);
        return "redirect:/";
    }

    @GetMapping("/tasks/edit/{id}")
    public String editTaskForm(@PathVariable Long id, Model model) {
        Optional<Task> task = taskRepository.findById(id);
        if (task.isPresent()) {
            model.addAttribute("task", task.get());
            return "task_form";
        } else {
            return "redirect:/";
        }
    }

    @PostMapping("/tasks/update/{id}")
    public String updateTask(@PathVariable Long id, @ModelAttribute Task task) {
        task.setId(id);
        taskRepository.save(task);
        return "redirect:/";
    }

    @GetMapping("/tasks/delete/{id}")
    public String deleteTask(@PathVariable Long id) {
        taskRepository.deleteById(id);
        return "redirect:/";
    }
}

@Entity
class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String title;
    private String description;

    // Конструктор по умолчанию
    public Task() {
    }

    // Конструктор с параметрами
    public Task(String title, String description) {
        this.title = title;
        this.description = description;
    }

    // Геттеры и сеттеры
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
}

@Repository
interface TaskRepository extends JpaRepository<Task, Long> {
}
