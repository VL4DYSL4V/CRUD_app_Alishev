package service;

import exception.ConnectionCannotBeEstablishedException;
import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

@Service
public class MySQLConnectionService implements ConnectionService{

    private static final String URL = "jdbc:mysql://localhost:3306/vladislav_db";
    private static final String USERNAME = "Vladislav";
    private static final String PASSWORD = "vladislavsDB14235662@Java";

    @Override
    public Connection getConnection() throws ConnectionCannotBeEstablishedException {
        try{
            Class.forName("com.mysql.jdbc.Driver");
            return DriverManager.getConnection(URL, USERNAME, PASSWORD);
        }catch (ClassNotFoundException | SQLException e){
            throw new ConnectionCannotBeEstablishedException(e);
        }
    }
}
