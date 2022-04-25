package com.mikebros.schedulehawk;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;

/**
 * The DB Connection class, used to simplify DB queries.
 */
public class DBConnection {
    private static final String databaseName = "client_schedule";
    private static final String DB_URL = "jdbc:mysql://localhost:9815/" + databaseName;
    private static final String username = "root";
    private static final String password = "secret";

    static Connection conn;

    /**
     * Make connection with the databaseName DB.
     *
     * @throws Exception the exception
     */
    public static void makeConnection() throws Exception {
        conn = DriverManager.getConnection(DB_URL, username, password);
    }

    /**
     * Close connection with the databaseName DB.
     *
     * @throws Exception the exception
     */
    public static void closeConnection() throws Exception {
        conn.close();
    }

    /**
     * Query result set.
     *
     * @param query a raw String SQL query
     * @return the result set of the query
     * @throws Exception the exception
     */
    public static ResultSet query(String query) throws Exception {
        makeConnection();
        return conn.createStatement().executeQuery(query);
    }

    /**
     * Run an update query.
     *
     * @param query the update query
     * @throws Exception the exception
     */
    public static void update(String query) throws Exception {
        makeConnection();
        conn.createStatement().executeUpdate(query);
    }
}
