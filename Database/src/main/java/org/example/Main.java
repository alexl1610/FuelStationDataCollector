package org.example;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Main {
    public static void main(String[] args) {

        String query = "SELECT * FROM customer";

        try (
                Connection conn = Database.getConnection();
                PreparedStatement ps = conn.prepareStatement(query);
                ResultSet rs = ps.executeQuery()
        ) {
            while (rs.next()) {
                int id = rs.getInt("id");
                String firstname = rs.getString("first_name");
                String lastname = rs.getString("last_name");

                User user = new User(id, firstname, lastname);

                System.out.println(user);
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}