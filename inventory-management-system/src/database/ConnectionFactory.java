package database;

import java.sql.*;

public class ConnectionFactory {
    private static final String driver = "com.mysql.cj.jdbc.Driver";
    private static final String url = "jdbc:mysql://localhost:3306/inventory_db";
    private static volatile ConnectionFactory instance;
    private final Connection connection;

    private ConnectionFactory(){
        try {
            Class.forName(driver);
            connection = DriverManager.getConnection(url, "root", "root123");
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public static ConnectionFactory getInstance(){
        ConnectionFactory result = instance;
        if(result == null){
            synchronized (ConnectionFactory.class){
                result = instance;
                if(result == null){
                    instance = result = new ConnectionFactory();
                }
            }
        }
        return result;
    }

    public Connection getConnection(){
        return connection;
    }
}
