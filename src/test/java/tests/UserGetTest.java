package tests;

import constants.Constants;
import io.qameta.allure.Owner;
import io.qameta.allure.TmsLink;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import lib.ApiCoreRequests;
import lib.Assertions;
import lib.BaseTestCase;
import lib.DataGenerator;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

@Owner("Billing-team")
@TmsLink("Get-user-data-tests-link")
@Tag("GET_USER_INFO_TESTS")
public class UserGetTest extends BaseTestCase {

    private final ApiCoreRequests apiCoreRequests = new ApiCoreRequests();

    @Test
    public void testGetUserDataNotAuth(){

        Response responseUserData = RestAssured
                .get(Constants.URL + "/user/2")
                .andReturn();

        Assertions.assertJsonHasField(responseUserData, "username");
        Assertions.assertJsonHasNotField(responseUserData, "firstName");
        Assertions.assertJsonHasNotField(responseUserData, "lastName");
        Assertions.assertJsonHasNotField(responseUserData, "email");
    }

    @Test
    public void testGetUserDetailsAuthAsSameUser(){
        Map<String, String> authData = new HashMap<>();
        authData.put("email", "vinkotov@example.com");
        authData.put("password", "1234");

        Response responseGetAuth = RestAssured
                .given()
                .body(authData)
                .post(Constants.URL + "/user/login")
                .andReturn();

        String header = this.getHeader(responseGetAuth, Constants.TOKEN_NAME);
        String cookie = this.getCookie(responseGetAuth, Constants.COOKIE_NAME);

        Response responseUserData = RestAssured
                .given()
                .header(Constants.TOKEN_NAME, header)
                .cookie(Constants.COOKIE_NAME, cookie)
                .get(Constants.URL + "/user/2")
                .andReturn();

        String[] expectedFields = {"username", "firstName", "lastName", "email"};
        Assertions.assertJsonHasFields(responseUserData, expectedFields);
    }

    @Test
    public void testGetUserDetailsAuthAsAnotherUser(){
        Map<String, String> authData = new HashMap<>();
        authData.put("email", "vinkotov@example.com");
        authData.put("password", "1234");
        authData = DataGenerator.getRegistrationData(authData);

        Response responseGetAuth = apiCoreRequests.makePostRequest(Constants.URL + "/user/login", authData);

        String header = this.getHeader(responseGetAuth, Constants.TOKEN_NAME);
        String cookie = this.getCookie(responseGetAuth, Constants.COOKIE_NAME);

        Response responseUserData = apiCoreRequests.makeGetRequest(
                Constants.URL + "/user/" + 123456,
                header,
                cookie);

        String expectedField = "username";
        Assertions.assertJsonHasField(responseUserData, expectedField);
        Assertions.assertJsonHasNotField(responseUserData, "firstName");
    }
}
