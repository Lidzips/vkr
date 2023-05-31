package com.example.demo.entity;


public class Task extends BaseEntity{
    private Integer complexity;
    private String taskText;
    private String file;
    private String prompt;
    private String topic;
    private String out1;
    private String out2;
    private String out3;

    public Task(Integer id, Integer complexity, String taskText, String file, String prompt, String topic, String out1, String out2, String out3) {
        super(id);
        this.complexity = complexity;
        this.taskText = taskText;
        this.file = file;
        this.prompt = prompt;
        this.topic = topic;
        this.out1 = out1;
        this.out2 = out2;
        this.out3 = out3;
    }
    public Integer getComplexity() {
        return complexity;
    }

    public void setComplexity(Integer complexity) {
        this.complexity = complexity;
    }

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }

    public String getTaskText() {
        return taskText;
    }

    public void setTaskText(String taskText) {
        this.taskText = taskText;
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
}
