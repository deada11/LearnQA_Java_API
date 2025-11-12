package tests;

import constants.Constants;
import io.qameta.allure.Description;
import io.qameta.allure.Owner;
import io.qameta.allure.TmsLink;
import io.restassured.response.Response;
import lib.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

@Owner("Activation-team")
@TmsLink("Delete-User-tests-link")
@Tag("DELETE_USER_TESTS")
public class UserDeleteTest extends BaseTestCase {

    private final ApiCoreRequests apiCoreRequests = new ApiCoreRequests();

    @Test
    @Description("Проверка удалить суперпользователя")
    @DisplayName("Try to delete superuser")
    public void testTryToDeleteSuperUser(){
        Response responseGetAuth = UserActions.loginAsSuperUser();

        Response deleteUser = apiCoreRequests.makeDeleteRequest(
                Constants.URL + "/user/" + getIntFromJson(responseGetAuth, "user_id"),
                getHeader(responseGetAuth, Constants.TOKEN_NAME),
                getCookie(responseGetAuth, Constants.COOKIE_NAME)
        );

        Assertions.assertJsonByName(deleteUser, "error",
                "Please, do not delete test users with ID 1, 2, 3, 4 or 5.");
    }

    @Test
    @Description("Проверка удаления пользователя и невозможности получить его данные")
    @DisplayName("Successful user delete")
    public void testSuccessfulDeleteUser(){
        // Generate user
        Map<String, String> userData = DataGenerator.getRegistrationData();
        String userId = UserActions.generateUserAndReturnId(userData);

        // Login
        Map<String, String> authData = new HashMap<>();
        authData.put("email", userData.get("email"));
        authData.put("password", userData.get("password"));
        Response responseGetAuth = UserActions.loginAsUser(authData);

        // Delete user
        Response deleteUser = apiCoreRequests.makeDeleteRequest(
                Constants.URL + "/user/" + userId,
                getHeader(responseGetAuth, Constants.TOKEN_NAME),
                getCookie(responseGetAuth, Constants.COOKIE_NAME)
        );

        Assertions.assertJsonHasField(deleteUser, "success");

        // Get info
        Response responseUserData = apiCoreRequests.makeGetRequest(
                Constants.URL + "/user/" + userId,
                getHeader(responseGetAuth, Constants.TOKEN_NAME),
                getCookie(responseGetAuth, Constants.COOKIE_NAME)
        );

        Assertions.assertResponseTextEquals(responseUserData, "User not found");
    }

    @Test
    @Description("Проверка невозможности удаления другого пользователя")
    @DisplayName("Try to delete another user")
    public void testTryToDeleteAnotherUser(){
        // Generate user
        Map<String, String> userData = DataGenerator.getRegistrationData();
        UserActions.generateUserAndReturnId(userData);

        // Login
        Map<String, String> authData = new HashMap<>();
        authData.put("email", userData.get("email"));
        authData.put("password", userData.get("password"));
        Response responseGetAuth = UserActions.loginAsUser(authData);

        // Delete user
        Response deleteUser = apiCoreRequests.makeDeleteRequest(
                Constants.URL + "/user/" + 123456,
                getHeader(responseGetAuth, Constants.TOKEN_NAME),
                getCookie(responseGetAuth, Constants.COOKIE_NAME)
        );

        Assertions.assertJsonByName(deleteUser, "error",
                "This user can only delete their own account.");
    }
}
