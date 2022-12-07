package core.models;

import core.utilities.DatabaseUtilities;
import core.utilities.Utilities;
import java.lang.reflect.Field;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;

public class Model {
    public int id;
    
    private String tableName;
    
    private Connection databaseConnection;
    
    private HashMap<String, String> fields = new HashMap<String, String>();
    
    private String QueryStatements = "";
    private String MutationStatement = "";
    
    public Model(){
        databaseConnection = Database.getConnection(); //Establish a database connection
        
        this.tableName = this.getClass().getSimpleName().toLowerCase(); //It gets the class name and use it as the table name
    }
    
    public Model get(){
        QueryStatements += "SELECT * FROM " + tableName + " "; 
        return this;
    }
    public Model and(){
        QueryStatements += "AND "; 
        return this;
    }
    public Model or(){
        QueryStatements += "OR "; 
        return this;
    }
    public Model where(String condition){
        QueryStatements += "WHERE " + condition + " "; 
        MutationStatement += "WHERE " + condition + " "; 
        return this;
    }
    public Model limit(Integer limit){
        QueryStatements += "LIMIT " + limit + " "; 
        return this;
    }
    
    public ArrayList runQueries() throws Exception{        
        ArrayList<Object> results = DatabaseUtilities.getDataFromDatabase(QueryStatements, databaseConnection, this);
        
        QueryStatements = ""; //to reset sql statements to start over
        
        return results;
    }
    
    public Model save() throws Exception {
        fields = Utilities.getObjectFields(this);
        String fieldsString = Utilities.setToString(",", fields.keySet());
        String variablesString = Utilities.createMultipleCharacters(",", "?", fields.keySet().size());
        
        MutationStatement = "INSERT INTO " + tableName + " (" + fieldsString + 
                    ") VALUES " + " (" + variablesString + ")";
        return this;
    }
    
    public Model update() throws Exception {
        fields = Utilities.getObjectFields(this);
        String str = "";
        for(HashMap.Entry<String, String> entry : fields.entrySet()){
            if(entry.getKey().equals("id")) continue;
            
            str += entry.getKey() + " = " + entry.getValue() + ",";
        }
        str = Utilities.removeLastCharacter(str);
        MutationStatement = "UPDATE " + tableName + " SET " + str + " ";
        return this;
    }
    
    public Model delete(){
        MutationStatement = "DELETE FROM " + tableName + " ";
        return this;
    }
    public void runMutation(){
        try(PreparedStatement statement = databaseConnection.prepareStatement(MutationStatement)){
            if(MutationStatement.contains("INSERT")){
                int i = 1;
                for(String value: fields.values()){
                    if(i == 1){
                        statement.setInt(1, Integer.parseInt(value));
                        i++;
                        continue;
                    }
                    statement.setString(i, String.valueOf(value));
                    i++;
                }

                if(statement.executeUpdate() > 0){
                    DatabaseUtilities.saveIdIntoObject(this, statement);
                }
            }else{
                statement.executeUpdate();            
            }
        }catch(Exception exception){
            System.out.println("Error occured: core/models/Model.java | runMutation(), " 
              + exception.getMessage());
        }
    }
    
   
    
    public <T> void migrate() throws Exception {
        fields = Utilities.getObjectFields(this);
        //create if not there before
        try(Statement statement = databaseConnection.createStatement()){
            String str = "";
            for(HashMap.Entry<String, String> entry : fields.entrySet()){
                T genericValue = Utilities.stringToNumber(entry.getValue());
                if(genericValue instanceof Integer){
                    str += entry.getKey() + " INT,";
                    continue;
                }
                str += entry.getKey() + " VARCHAR(255),";
            }
            
            String sql = "CREATE TABLE " + 
             tableName +
             "(id INT NOT NULL AUTO_INCREMENT, " +
             str +
             " PRIMARY KEY ( id ))"; 
            
            statement.executeUpdate(sql);
            
        }catch(Exception exception){
            // alter the table

        }
        
    }
}
