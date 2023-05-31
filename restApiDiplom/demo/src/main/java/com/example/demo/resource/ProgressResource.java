package com.example.demo.resource;

import com.example.demo.entity.Progress;

public class ProgressResource extends BaseResource{
    private Integer id;
    private Integer userId;
    private Integer taskId;
    private Boolean completed;
    private Boolean available;

    public ProgressResource() {}

    public ProgressResource(Progress progress) {
        this.id = progress.getId();
        this.userId = progress.getUserId();
        this.taskId = progress.getTaskId();
        this.completed = progress.getCompleted();
        this.available = progress.getAvailable();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getTaskId() {
        return taskId;
    }

    public void setTaskId(Integer taskId) {
        this.taskId = taskId;
    }

    public Boolean getCompleted() {
        return completed;
    }

    public void setCompleted(Boolean completed) {
        this.completed = completed;
    }

    public Boolean getAvailable() {
        return available;
    }

    public void setAvailable(Boolean available) {
        this.available = available;
    }

    public Progress toEntity() {
        return new Progress(
                this.id,
                this.userId,
                this.taskId,
                this.completed,
                this.available
        );
    }
}
