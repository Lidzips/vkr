package com.example.demo.resource;

import com.example.demo.entity.Task;

public class TaskResource extends BaseResource{
    private Integer id;
    private Integer complexity;
    private String taskText;
    private String file;
    private String prompt;
    private String topic;
    private String out1;
    private String out2;
    private String out3;

    public TaskResource() {}

    public TaskResource(Task task) {
        this.id = task.getId();
        this.complexity = task.getComplexity();
        this.taskText = task.getTaskText();
        this.file = task.getFile();
        this.prompt = task.getPrompt();
        this.topic = task.getTopic();
        this.out1 = task.getOut1();
        this.out2 = task.getOut2();
        this.out3 = task.getOut3();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getComplexity() {
        return complexity;
    }

    public void setComplexity(Integer complexity) {
        this.complexity = complexity;
    }

    public String getTaskText() {
        return taskText;
    }

    public void setTaskText(String taskText) {
        this.taskText = taskText;
    }

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }

    public String getPrompt() {
        return prompt;
    }

    public void setPrompt(String prompt) {
        this.prompt = prompt;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getOut1() {
        return out1;
    }

    public void setOut1(String out1) {
        this.out1 = out1;
    }

    public String getOut2() {
        return out2;
    }

    public void setOut2(String out2) {
        this.out2 = out2;
    }

    public String getOut3() {
        return out3;
    }

    public void setOut3(String out3) {
        this.out3 = out3;
    }

    public Task toEntity(){
        return new Task(
                this.id,
                this.complexity,
                this.taskText,
                this.file,
                this.prompt,
                this.topic,
                this.out1,
                this.out2,
                this.out3
        );
    }
}
