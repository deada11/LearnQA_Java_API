package lib;

import constants.Constants;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;

import java.util.HashMap;
import java.util.Map;

public class UserActions {

    private static final ApiCoreRequests apiCoreRequests = new ApiCoreRequests();

    public static String generateUserAndReturnId(Map<String, String> userData){

        JsonPath responseCreateAuth = RestAssured
                .given()
                .body(userData)
                .post(Constants.URL + "/user")
                .jsonPath();

        return responseCreateAuth.getString("id");
    }

    public static Response loginAsSuperUser(){
        Map<String, String> authData = new HashMap<>();
        authData.put("email", "vinkotov@example.com");
        authData.put("password", "1234");

        return apiCoreRequests
                .makePostRequest(Constants.URL + "/user/login", authData);
    }

    public static Response loginAsUser(Map<String, String> authData){
        return apiCoreRequests.
                makePostRequest(Constants.URL + "/user/login", authData);
    }
}
