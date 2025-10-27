package com.taskmanager.service;

import java.util.Scanner;

public interface ITaskManager {
    public void addTask(Scanner sc);
    public void showTasks();
    public void deleteTask(Scanner sc);
}
