package core.utilities;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

public class Utilities {
    public static String arrayToString(String delimeter, String[] arr){
        return String.join(delimeter, arr);
    }
    public static String listToString(String delimeter, List list){
        return String.join(delimeter, list);
    }
    public static String setToString(String delimeter, Set<String> set){
        return String.join(delimeter, set);
    }
    
    public static String createMultipleCharacters(String delimeter, String character, Integer count){
        String str = "";
        
        for(int i = 0; i < count; i++){
            str += character + "" + delimeter;
        }

        return removeLastCharacter(str);
    }
    
    public static String removeLastCharacter(String str){
        if(str == null) return null;
        if(str.length() <= 0) return null;
       
        return str.substring(0, str.length() - 1);
    }
    
    public static String replaceLastCharacter(String str, String character){
        if(str == null) return null;
        if(str.length() <= 0) return null;
       
        return str.substring(0, str.length() - 1) + character;
    }
    
    public static <T> T stringToNumber(String str) {
        if(str == null || str.equals("")) return null;
        
        try {
            Integer number = Integer.parseInt(str);
            return (T)number;
        } catch (Exception exception) {
            return (T)str;
        }
    }
    
    public static String objectFieldsNamesAsString(String delimeter, Object object) throws Exception{
        String str = "";
        
        Field[] fields = object.getClass().getDeclaredFields();
        
        for(Field field: fields){
            str +=  field.getName() + delimeter;
        }
        
        return removeLastCharacter(str);
    }
    
    public static String objectFieldsValuesAsString(String delimeter, Object object) throws Exception{
        String str = "";
        
        Field[] fields = object.getClass().getDeclaredFields();
        
        for(Field field: fields){
            str +=  field.get(object) + delimeter;
        }
        
        return removeLastCharacter(str);
    }
    
    public static <T> HashMap<T, T> getObjectFields(Object object) throws Exception {
        HashMap hashMap = new HashMap<T, T>();
        
        Field[] fields = object.getClass().getDeclaredFields();
        for(Field field: fields){
            hashMap.put(field.getName(), field.get(object));
        }
        
        return hashMap;
    }
   
}
