package tests;

import constants.Constants;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import lib.Assertions;
import lib.BaseTestCase;
import lib.DataGenerator;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

public class UserEditTest extends BaseTestCase {

    @Test
    public void testEditJustCreateTest(){
        // Generate user
        Map<String, String> userData = DataGenerator.getRegistrationData();

        JsonPath responseCreateAuth = RestAssured
                .given()
                .body(userData)
                .post(Constants.URL + "/user")
                .jsonPath();

        String userId = responseCreateAuth.getString("id");

        // Login
        Map<String, String> authData = new HashMap<>();

        authData.put("email", userData.get("email"));
        authData.put("password", userData.get("password"));

        Response responseGetAuth = RestAssured
                .given()
                .body(authData)
                .post(Constants.URL + "/user/login")
                .andReturn();

        // Edit
        String newName = "Changed Name";
        Map<String, String> editData = new HashMap<>();
        editData.put("firstName", newName);

        Response responseEditUser = RestAssured
                .given()
                .header("x-csrf-token", getHeader(responseGetAuth, "x-csrf-token"))
                .cookie("auth_sid", getCookie(responseGetAuth, "auth_sid"))
                .body(editData)
                .put(Constants.URL + "/user/" + userId)
                .andReturn();

        // Get
        Response responseUserData = RestAssured
                .given()
                .header("x-csrf-token", getHeader(responseGetAuth, "x-csrf-token"))
                .cookie("auth_sid", getCookie(responseGetAuth, "auth_sid"))
                .get(Constants.URL + "/user/" + userId)
                .andReturn();

        Assertions.assertJsonByName(responseUserData, "firstName", newName);
    }
}
