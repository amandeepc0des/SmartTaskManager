package com.taskmanager;
import com.taskmanager.model.enums.Choice;
import com.taskmanager.service.DataBaseConnection;
import com.taskmanager.service.TaskManager;

import java.sql.Connection;
import java.sql.SQLException;
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
                case FILTERTASKS:
                    // logic for filtering the tasks
                    taskManager.filterTasks(sc);
                    break;
                case EXIT:
                    // logic for exiting
                    System.out.println("SmartTaskManager is exited by User.");
                    isCompleted = true;
                    break;
                case DBCONNECT:
                {
                    DataBaseConnection db = new DataBaseConnection();
                    Connection con = null;
                    try
                    {
                        con = db.getConnection();
                    }
                    catch(SQLException e)
                    {
                        System.out.println("Exception occurred: " + e.getMessage());
                    }

                    if(con != null) System.out.println("Connection is established ");
                    else System.out.println("Connection failed.");

                    break;
                }
                default:
                    System.out.println("Invalid choice is selected");
                    break;
            }
            System.out.println("______________________");
        }

    }

    private static void showOptions()
    {
        System.out.println("1. Add Task");
        System.out.println("2. View Tasks");
        System.out.println("3. Delete Task");
        System.out.println("4. Filter Tasks");
        System.out.println("5. Connect Database");
        System.out.println("6. Exit");
        System.out.println("Enter choice");
    }

}
