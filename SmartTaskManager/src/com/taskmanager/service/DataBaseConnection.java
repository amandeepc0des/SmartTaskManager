package com.taskmanager.service;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DataBaseConnection {
    private static final String URL = "jdbc:postgresql://localhost:5432/SmartTaskManager";
    private static final String USER = "postgres";
    private static final String PASSWORD = "12345";

    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

}
