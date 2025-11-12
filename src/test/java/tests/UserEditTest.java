package tests;

import constants.Constants;
import io.qameta.allure.Description;
import io.qameta.allure.Owner;
import io.qameta.allure.TmsLink;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import lib.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

@Owner("Activation-team")
@TmsLink("Editing-user-tests-link")
@Tag("EDITING_USER_TESTS")
public class UserEditTest extends BaseTestCase {

    private static final ApiCoreRequests apiCoreRequests = new ApiCoreRequests();

    @Test
    @Description("Проверка изменения вновь созданного пользователя")
    @DisplayName("Try to edit just created user")
    public void testEditJustCreateTest(){
        // Generate user
        Map<String, String> userData = DataGenerator.getRegistrationData();
        String userId = UserActions.generateUserAndReturnId(userData);

        // Login
        Map<String, String> authData = new HashMap<>();
        authData.put("email", userData.get("email"));
        authData.put("password", userData.get("password"));
        Response responseGetAuth = UserActions.loginAsUser(authData);

        // Edit
        String newName = "Changed Name";
        Map<String, String> editData = new HashMap<>();
        editData.put("firstName", newName);

        apiCoreRequests.makePutRequest(
                Constants.URL + "/user/" + userId,
                getHeader(responseGetAuth, Constants.TOKEN_NAME),
                getCookie(responseGetAuth, Constants.COOKIE_NAME),
                editData
        );

        // Get
        Response responseUserData = apiCoreRequests.makeGetRequest(
                Constants.URL + "/user/" + userId,
                getHeader(responseGetAuth, Constants.TOKEN_NAME),
                getCookie(responseGetAuth, Constants.COOKIE_NAME)
        );

        Assertions.assertJsonByName(responseUserData, "firstName", newName);
    }

    @Test
    @Description("Проверка изменить пользователя будучи неавторизованным")
    @DisplayName("Try to update user without authorisation")
    public void testTryToUpdateWithoutAuthorisation(){
        Map<String, String> editData = new HashMap<>();
        editData.put("username", "test name");

        Response responseEditUnauthorisedUser = RestAssured
                .given()
                .header(Constants.TOKEN_NAME, "")
                .cookie(Constants.COOKIE_NAME, "")
                .body(editData)
                .put(Constants.URL + "/user/" + 123456)
                .andReturn();

        Assertions.assertJsonByName(responseEditUnauthorisedUser, "error", "Auth token not supplied");
    }

    @Test
    @Description("Проверка обновления другого пользователя будучи авторизованным")
    @DisplayName("Try to update another user")
    public void testTryToUpdateAnotherUser(){
        // Generate user
        Map<String, String> userData = DataGenerator.getRegistrationData();
        UserActions.generateUserAndReturnId(userData);

        // Login
        Map<String, String> authData = new HashMap<>();
        authData.put("email", userData.get("email"));
        authData.put("password", userData.get("password"));
        Response responseGetAuth = UserActions.loginAsUser(authData);

        // Edit
        String newName = "Changed Name";

        Map<String, String> editData = new HashMap<>();
        editData.put("firstName", newName);

        Response responseEditUser = apiCoreRequests.makePutRequest(
                Constants.URL + "/user/" + 444,
                getHeader(responseGetAuth, Constants.TOKEN_NAME),
                getCookie(responseGetAuth, Constants.COOKIE_NAME),
                editData
        );

        Assertions.assertJsonByName(responseEditUser, "error",
                "This user can only edit their own data.");
    }

    @Test
    @Description("Проверка изменения email на значение без @")
    @DisplayName("Try to update email without @")
    public void testTryToUpdateEmailWithoutAt(){
        // Generate user
        Map<String, String> userData = DataGenerator.getRegistrationData();
        String userId = UserActions.generateUserAndReturnId(userData);

        // Login
        Map<String, String> authData = new HashMap<>();
        authData.put("email", userData.get("email"));
        authData.put("password", userData.get("password"));
        Response responseGetAuth = UserActions.loginAsUser(authData);

        // Edit
        Map<String, String> editData = new HashMap<>();
        editData.put("email", "test.example.com");

        Response responseEditUser = apiCoreRequests.makePutRequest(
                Constants.URL + "/user/" + userId,
                getHeader(responseGetAuth, Constants.TOKEN_NAME),
                getCookie(responseGetAuth, Constants.COOKIE_NAME),
                editData
        );

        Assertions.assertJsonByName(responseEditUser, "error", "Invalid email format");
    }

    @Test
    @Description("Проверка изменения имени пользователя на очень короткое значение")
    @DisplayName("Try to update firstName to short value")
    public void testTryToUpdateFirstNameToShortValue(){
        // Generate user
        Map<String, String> userData = DataGenerator.getRegistrationData();
        String userId = UserActions.generateUserAndReturnId(userData);

        // Login
        Map<String, String> authData = new HashMap<>();
        authData.put("email", userData.get("email"));
        authData.put("password", userData.get("password"));
        Response responseGetAuth = UserActions.loginAsUser(authData);

        //Edit

        Map<String, String> editData = new HashMap<>();
        editData.put("firstName", "t");

        Response responseEditUser = apiCoreRequests.makePutRequest(
                Constants.URL + "/user/" + userId,
                getHeader(responseGetAuth, Constants.TOKEN_NAME),
                getCookie(responseGetAuth, Constants.COOKIE_NAME),
                editData
        );

        Assertions.assertJsonByName(responseEditUser, "error",
                "The value for field `firstName` is too short");
    }
}
