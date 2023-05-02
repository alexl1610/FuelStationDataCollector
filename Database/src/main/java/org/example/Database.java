package org.example;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Database {

    private final static String DRIVER = "postgresql";
    private final static String HOST = "localhost";
    private final static int PORT = 30001;
    private final static String DATABASE_NAME = "customerdb";
    private final static String USERNAME = "postgres";
    private final static String PASSWORD = "postgres";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(getUrl());
    }

    private static String getUrl() {
        return String.format(
                "jdbc:%s://%s:%s/%s?user=%s&password=%s",
                DRIVER,
                HOST,
                PORT,
                DATABASE_NAME,
                USERNAME,
                PASSWORD
        );
    }
}
