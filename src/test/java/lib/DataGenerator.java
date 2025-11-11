package lib;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

public class DataGenerator {

    public static final String SHORT_VALUE = "t";
    public static final String LONG_VALUE = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, " +
            "sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, " +
            "quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor";

    public static void makeUserDataShort(Map<String, String> userData, String parameter){
        userData.replace(parameter, SHORT_VALUE);
    }

    public static void makeUserDataLong(Map<String, String> userData, String parameter){
        userData.replace(parameter, LONG_VALUE);
    }

    public static String getRandomEmail(){
        String timestamp = new SimpleDateFormat("yyyyMMddHHmmss").format(new java.util.Date());
        return "learnQa" + timestamp + "@example.com";
    }

    public static Map<String, String> getRegistrationData(){
        Map<String, String> data = new HashMap<>();

        data.put("email", getRandomEmail());
        data.put("password", "123");
        data.put("username", "learnqa");
        data.put("firstName", "learnqa");
        data.put("lastName", "learnqa");

        return data;
    }

    public static Map<String, String> getRegistrationData(Map<String, String> nonDefaultValues){
        Map<String, String> defaultValues = getRegistrationData();

        Map<String, String> userData = new HashMap<>();
        String[] keys = {"email", "password", "username", "firstName", "lastName"};
        for (String key : keys){
            if (nonDefaultValues.containsKey(key)){
                userData.put(key, nonDefaultValues.get(key));
            } else {
                userData.put(key, defaultValues.get(key));
            }
        }
        return userData;
    }
}
