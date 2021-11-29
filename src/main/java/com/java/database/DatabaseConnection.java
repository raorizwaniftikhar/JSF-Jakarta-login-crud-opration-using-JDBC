package com.java.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Use MySQL Database for store data
 *
 * User Singleton Design Pattern , allow only one object creation.
 */
public class DatabaseConnection {
    private static DatabaseConnection instance;
    Connection connection;
    //local machine address
    private final static String HOST = "localhost";
    //Database port no
    private final static int MYSQL_PORT = 3306;
    //Database Table name
    public final static String MYSQL_DATABASE = "student";
    //Database Connection URL String
    private final static String MYSQL_URL = "jdbc:mysql://" + HOST + ":" + MYSQL_PORT + "/" + MYSQL_DATABASE;
    //Default Database username
    private final static String MYSQL_USERNAME = "root";
    //Database password---please add your Database password
    private final static String MYSQL_PASSWORD = "Madina92";

    private DatabaseConnection() {
    }

    //Create Database Connection
    public Connection getMysqlConnection() throws ClassNotFoundException, SQLException {
        Class.forName("com.mysql.cj.jdbc.Driver");
        return this.connection = DriverManager.getConnection(MYSQL_URL, MYSQL_USERNAME, MYSQL_PASSWORD);
    }

    public static DatabaseConnection getInstance() {
        if (instance == null) {
            instance = new DatabaseConnection();
        } else {
            return instance;
        }

        return instance;
    }

}
