package org.example;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Database {

    private final static String DRIVER = "postgresql";
    private static String DB_URL = "";
    private final static String DATABASE_NAME = "stationdb";
    private final static String USERNAME = "postgres";
    private final static String PASSWORD = "postgres";

    public static Connection getConnection(String db_url) throws SQLException {
        DB_URL = db_url;
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

    public static List<Charge> getStationData(String dbUrl) {
        String query = "SELECT * FROM charge";
        List<Charge> charges = new ArrayList<>();

        try (Connection conn = Database.getConnection(dbUrl);
             PreparedStatement ps = conn.prepareStatement(query)) {
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                int id = rs.getInt("id");
                int kwH = rs.getInt("kwh");
                int res_customer_id = rs.getInt("customer_id");

                Charge charge = new Charge(id, kwH, res_customer_id);
                charges.add(charge);

            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return charges;
    }
}

