package core.models;

import java.sql.*;
import configurations.Configurations;

public class Database {
    private static Database instance = null;
    public Connection connection;
    
    private Database()
    {
        try {
            connection = DriverManager.getConnection(
               "jdbc:"+ 
               Configurations.DATABASE_TYPE 
               +"://"+ 
               Configurations.DATABASE_HOST
               +":"+
               Configurations.DATABASE_PORT
               +"/"+
               Configurations.DATABASE_NAME,
               Configurations.DATABASE_USER, 
               Configurations.DATABASE_PASSWORD
            ); 
            
            System.out.println("Connected To Database " + 
                Configurations.DATABASE_NAME + " Succesfully!");
            
        }catch(Exception exception){
            System.out.println("Error occured: core/models/Datbase.java, " 
                    + exception.getMessage());
        }
    }
  
    public static Connection getConnection()
    {
        if (instance == null) instance = new Database();
              
        return instance.connection;
    }   
}