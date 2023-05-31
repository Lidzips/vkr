package com.example.demo.controller;

import com.example.demo.entity.Task;
import com.example.demo.repository.TaskRepository;
import com.example.demo.resource.TaskResource;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;

@RestController
@RequestMapping(value = "/task")
public class TaskController {
    private final TaskRepository taskRepository;

    public TaskController(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    TaskResource get(@PathVariable Integer id) {
        Task entity = taskRepository.select(id);
        if (entity == null) return null;
        return new TaskResource(entity);
    }

    @RequestMapping(value = "", method = RequestMethod.GET)
    TaskResource[] getAll() {
        Task[] tasks;
        tasks = taskRepository.select();
        return Arrays.stream(tasks)
                .map(entity -> {
                    return new TaskResource(entity);
                })
                .toArray(TaskResource[]::new);
    }

    @RequestMapping(value = "", method = RequestMethod.POST)
    TaskResource post(@RequestBody TaskResource resource) {
        Task entity = taskRepository.insert(resource.toEntity());
        if (entity == null) return null;
        return new TaskResource(entity);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    TaskResource put(@PathVariable Integer id,
                         @RequestBody TaskResource resource) {
        Task entity = taskRepository.update(id, resource.toEntity());
        if (entity == null) return null;
        return new TaskResource(entity);
    }
}
