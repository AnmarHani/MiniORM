package core.utilities;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.*;
import java.util.ArrayList;


public class DatabaseUtilities {
   public static void saveIdIntoObject(Object object, PreparedStatement statement) throws Exception{
        Field field = object.getClass().getField("id");
        ResultSet generatedKeys = statement.getGeneratedKeys();
        if (generatedKeys.next()) {
            field.setInt(object, generatedKeys.getInt(1));
        }
   } 
   public static ArrayList convertResultsToObjects(ResultSet results, Object object) throws Exception {
        ArrayList<Object> objectsArray = new ArrayList<Object>();
        Constructor constructor = object.getClass().getDeclaredConstructor();
//            for(int i = 0; i < object.getClass().getFields().length; i++){ //loop through all fields
        while(results.next()) { // loop through every row
            Object instance = constructor.newInstance();
            Field[] instanceFields = instance.getClass().getDeclaredFields();
            int i = 1; //column 1 in the same row
            for(Field field: instanceFields){ //loop through instace fields       
                    if(i == 1){
                        field.set(instance, results.getInt(1)); //to assign the id
                        i++;
                        continue;
                    }
                    field.set(instance, results.getString(i)); //getString = column //one field in the same instance. assign its value = column[i]
                    i++;
            }
            objectsArray.add(instance);
        }
//        System.out.println(results.getString(2));
//        }
        return objectsArray;
   }
   
   public static ArrayList getDataFromDatabase(String sql, Connection databaseConnection, Object object){
      ArrayList<Object> results = new ArrayList<Object>();
      
      try (Statement statement = databaseConnection.createStatement()) {
        ResultSet result = statement.executeQuery(sql);
        
        results = DatabaseUtilities.convertResultsToObjects(result, object);
      }catch(Exception exception){
        System.out.println("Error occured: core/utilities/DatabaseUtilites.java | getDataFromDatabase(), " 
          + exception.getMessage());
      }
      
      return results;
   }
   
   public static void saveDataToDatabase(String sql){
       
   }
   
   public static void updateDataToDatabase(String sql){
       
   }
   
   public static void deleteDataInDatabase(String sql){
       
   }
}
