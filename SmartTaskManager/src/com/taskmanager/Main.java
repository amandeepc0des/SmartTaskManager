package com.taskmanager;
import com.taskmanager.model.enums.Choice;
import com.taskmanager.service.TaskManager;

import java.util.Scanner;

public class Main {
    public static void main(String [] args)
    {
        System.out.println("Smart Task Manager");
        System.out.println("______________________");
        boolean isCompleted = false;
        TaskManager taskManager =  new TaskManager();
        while(!isCompleted)
        {
            showOptions();
            Scanner sc = new Scanner(System.in);
            int options = sc.nextInt();
            Choice choice =  Choice.values()[options - 1];
            switch (choice)
            {
                case ADDTASK:
                    // logic for adding task
                    taskManager.addTask(sc);
                    break;
                case SHOWTASKS:
                    // logic for showing the tasks
                    taskManager.showTasks();
                    break;
                case DELETETASK:
                    // logic for deleting the tasks
                    taskManager.deleteTask(sc);
                    break;
                case EXIT:
                    // logic for exiting
                    System.out.println("SmartTaskManager is exited by User.");
                    isCompleted = true;
                    break;
                default:
                    System.out.println("Invalid choice is selected");
            }
            System.out.println("______________________");
        }

    }

    private static void showOptions()
    {
        System.out.println("1. Add Task");
        System.out.println("2. View Tasks");
        System.out.println("3. Delete Task");
        System.out.println("4. Exit");
        System.out.println("Enter choice");
    }

}
