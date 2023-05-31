package com.example.demo.entity;

public class Progress extends BaseEntity{
    private Integer userId;
    private Integer taskId;
    private Boolean completed;
    private Boolean available;

    public Progress (Integer id, Integer userId, Integer taskId, Boolean completed, Boolean available) {
        super(id);
        this.userId = userId;
        this.taskId = taskId;
        this.completed = completed;
        this.available = available;
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
}
