package org.example;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Database {

    private final static String DRIVER = "postgresql";
    private final static String DB_URL = "localhost:30002";
    private final static String DATABASE_NAME = "stationdb";
    private final static String USERNAME = "postgres";
    private final static String PASSWORD = "postgres";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(getUrl());
    }

    private static String getUrl() {
        return String.format(
                "jdbc:%s://%s/%s?user=%s&password=%s",
                DRIVER,
                DB_URL,
                DATABASE_NAME,
                USERNAME,
                PASSWORD
        );
    }

    public static List<String> getDBUrls() {
        String query = "SELECT db_url FROM station";
        List<String> dbUrls = new ArrayList<>();

        try (
                java.sql.Connection conn = Database.getConnection();
                PreparedStatement ps = conn.prepareStatement(query);
                ResultSet rs = ps.executeQuery()
        ) {
            while (rs.next()) {
                String dbUrl = rs.getString("db_url");
                dbUrls.add(dbUrl);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return dbUrls;
    }
}

