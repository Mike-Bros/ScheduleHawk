package com.mikebros.schedulehawk;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;

public class DBConnection {
    private static final String databaseName = "client_schedule";
    private static final String DB_URL = "jdbc:mysql://localhost:9815/" + databaseName;
    private static final String username = "root";
    private static final String password = "secret";
    static Connection conn;

    public static void makeConnection() throws Exception {
        conn = DriverManager.getConnection(DB_URL, username, password);
    }

    public static void closeConnection() throws Exception {
        conn.close();
    }

    public static ResultSet query(String query) throws Exception {
        makeConnection();
        return conn.createStatement().executeQuery(query);
    }
}
