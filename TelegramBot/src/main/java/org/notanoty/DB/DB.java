package org.notanoty.DB;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


public class DB {
    public static Connection connect() throws SQLException {

        try {

            // Get database credentials from DatabaseConfig class
            var jdbcUrl = "jdbc:postgresql://localhost:5432/postgres";
//            DatabaseConfig.getDbUrl();
            var user = "postgres";
//            DatabaseConfig.getDbUsername();
            var password = "postgres";
//                    DatabaseConfig.getDbPassword();

            // Open a connection
            return DriverManager.getConnection(jdbcUrl, user, password);

        } catch (SQLException  e) {
            System.err.println(e.getMessage());
            return null;
        }
    }
}

