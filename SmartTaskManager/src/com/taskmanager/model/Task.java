package com.taskmanager.model;
import com.taskmanager.model.enums.Category;
import com.taskmanager.model.enums.TaskStatus;
import java.time.LocalDate;
public class Task {
    private String title;
    private Category category;
    private TaskStatus status;
    private LocalDate dueDate;

    public Task(String title, Category category, TaskStatus status, LocalDate dueDate)
    {
        this.title = title;
        this.category = category;
        this.status = status;
        this.dueDate = dueDate;
    }

    public void updateStatus(TaskStatus newStatus)
    {
        this.status = newStatus;
    }

    public String getTitle()
    {
        return this.title;
    }

    public Category getCategory()
    {
        return this.category;
    }

    public TaskStatus getStatus()
    {
        return this.status;
    }

    @Override
    public String toString() {
        return "Title: " + title + ", Category: " + category + ", Due Date: " + dueDate + ", Status: " + status + "\n";
    }
}
