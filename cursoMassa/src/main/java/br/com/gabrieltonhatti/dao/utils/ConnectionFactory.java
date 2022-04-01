package br.com.gabrieltonhatti.dao.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionFactory {

    private static Connection conn;

    private static final String host = "localhost";
    private static final String port = "5433";
    private static final String schema = "dbunit";
    private static final String user = System.getenv("DATABASE_USER");
    private static final String password = System.getenv("DATABASE_PASSWORD");

    public static Connection getConnection() throws SQLException, ClassNotFoundException {
        if (conn == null) {
            conn = DriverManager.getConnection(
                    "jdbc:postgresql://" + host + ":" + port + "/" + schema + "?user=" + user + "&password=" + password);
        }

        return conn;
    }

    public static void closeConnection() throws SQLException {
        if (conn != null) {
            conn.close();
        }
        conn = null;
    }

}
