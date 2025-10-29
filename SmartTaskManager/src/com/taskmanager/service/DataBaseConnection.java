package com.taskmanager.service;

import java.sql.Connection;

public class DataBaseConnection {
    public Connection makeConnection(String db, String user, String pass)
    {
        
        try (Connection con = DriverManager.getConnection(url, user, password)) {
            System.out.println("Connected to PostgreSQL successfully!");
        } catch (SQLException e) {
            System.err.println("Connection failed: " + e.getMessage());
        }

    }
}
