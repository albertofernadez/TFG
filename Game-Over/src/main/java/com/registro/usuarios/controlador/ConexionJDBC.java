package com.registro.usuarios.controlador;

import java.sql.Connection;
import java.sql.DriverManager;


public class ConexionJDBC {
    private static final String URL = "jdbc:mysql://localhost:3306/gameover?serverTimezone=UTC";
    private static final String USER = "root";
    private static final String PASSWORD = "";
    
    public static Connection getConnection() throws Exception {
        Class.forName("com.mysql.cj.jdbc.Driver");
        Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
        return connection;
    }
}

