package org.example;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Database {
    private final static String DRIVER = "postgresql";
    private final static String DATABASE_NAME = "customerdb";
    private final static String USERNAME = "postgres";
    private final static String PASSWORD = "postgres";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(getUrl());
    }

    private static String getUrl() {
        String DB_URL = "localhost:30001";
        return String.format(
                "jdbc:%s://%s/%s?user=%s&password=%s",
                DRIVER,
                DB_URL,
                DATABASE_NAME,
                USERNAME,
                PASSWORD
        );
    }

    public static String getCustomerData(String id) {
        String query = "SELECT * FROM customer WHERE id =" + id;
        String firstName = "Max", lastName = "Mustermann";

        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                firstName = rs.getString("first_name");
                lastName = rs.getString("last_name");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return String.format("%s %s", firstName, lastName);
    }
}
