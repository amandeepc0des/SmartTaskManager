package com.taskmanager.service;

import com.taskmanager.model.Task;
import com.taskmanager.model.enums.Category;
import com.taskmanager.model.enums.TaskStatus;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class TaskManager implements ITaskManager{
    private HashMap<Integer, Task> taskDictionary;

    public TaskManager()
    {
        taskDictionary = new HashMap<>();
    }

    public void addTask(Scanner sc)
    {
        // add logic for adding the task;
        System.out.println("Enter the title");
        sc.nextLine();
        String title =  sc.nextLine();
        System.out.println("Select the Category");
        for(Category item :  Category.values())
        {
            System.out.println(item.ordinal() + 1 + ". " + item);
        }
        int cat = sc.nextInt();
        Category category = Category.values()[cat - 1];
        System.out.println("Select the Status");
        for(TaskStatus item : TaskStatus.values())
        {
            System.out.println(item.ordinal() + 1 + ". " + item);
        }
        int stat = sc.nextInt();
        TaskStatus status = TaskStatus.values()[stat - 1];
        LocalDate dueDate = LocalDate.now();

        Task task = new Task(title, category, status, dueDate);
        taskDictionary.put(taskDictionary.size() + 1, task);
        System.out.println("Task added Successfully" + ", Dictionary Size " + ": " + taskDictionary.size());
    }

    public void showTasks()
    {
        // add logic to show the tasks;
        System.out.println("All Tasks are listed Below");
        for(int item : taskDictionary.keySet())
        {
            Task task = taskDictionary.get(item);
            System.out.print(item + ": " + task);
        }
    }

    public void deleteTask(Scanner sc)
    {
        // add logic to delete the task;
        if(taskDictionary.isEmpty())
        {
            System.out.println("No task are added yet, Delete operation is invalid");
            return;
        }

        showTasks();
        try
        {
            System.out.println("Select the task to delete");
            int task = sc.nextInt();
            String taskTitle = taskDictionary.get(task).getTitle();
            taskDictionary.remove(task);
            System.out.println("Task " + taskTitle + " is removed successfully");
        }
        catch(Exception e)
        {
            System.out.println("Exception Found: " + e.getMessage() + " No task was deleted");
        }

    }
}
