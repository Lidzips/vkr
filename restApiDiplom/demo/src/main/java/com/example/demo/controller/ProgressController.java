package com.example.demo.controller;

import com.example.demo.entity.Progress;
import com.example.demo.repository.ProgressRepository;
import com.example.demo.resource.ProgressResource;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;

@RestController
@RequestMapping(value = "/progress")
public class ProgressController {
    private final ProgressRepository progressRepository;

    public ProgressController(ProgressRepository progressRepository) {
        this.progressRepository = progressRepository;
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    ProgressResource get(@PathVariable Integer id) {
        Progress entity = progressRepository.select(id);
        if (entity == null) return null;
        return new ProgressResource(entity);
    }

    @RequestMapping(value = "", method = RequestMethod.GET)
    ProgressResource[] getAll() {
        Progress[] alphabets;
        alphabets = progressRepository.select();
        return Arrays.stream(alphabets)
                .map(entity -> {
                    return new ProgressResource(entity);
                })
                .toArray(ProgressResource[]::new);
    }

    @RequestMapping(value = "/by_user", method = RequestMethod.GET)
    ProgressResource[] getAllByUser(@RequestParam("user_id") Integer userId) {
        Progress[] alphabets;
        alphabets = progressRepository.selectByUserId(userId);
        return Arrays.stream(alphabets)
                .map(entity -> {
                    return new ProgressResource(entity);
                })
                .toArray(ProgressResource[]::new);
    }

    @RequestMapping(value = "/usertask", method = RequestMethod.GET)
    ProgressResource getByUserAndTask(@RequestParam("user_id") Integer userId,
    @RequestParam("task_id") Integer taskId) {
        Progress entity = progressRepository.selectIdByUserAndTask(userId, taskId);
        if (entity == null) return null;
        return new ProgressResource(entity);
    }

    @RequestMapping(value = "", method = RequestMethod.POST)
    ProgressResource post(@RequestBody ProgressResource resource) {
        Progress entity = progressRepository.insert(resource.toEntity());
        if (entity == null) return null;
        return new ProgressResource(entity);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    ProgressResource put(@PathVariable Integer id,
                         @RequestBody ProgressResource resource) {
        Progress entity = progressRepository.update(id, resource.toEntity());
        if (entity == null) return null;
        return new ProgressResource(entity);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    ProgressResource delete(@PathVariable Integer id) {
        Progress entity = progressRepository.delete(id);
        if (entity == null) return null;
        return new ProgressResource(entity);
    }

}
