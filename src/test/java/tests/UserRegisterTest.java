package tests;

import constants.Constants;
import io.qameta.allure.Owner;
import io.qameta.allure.TmsLink;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import lib.Assertions;
import lib.BaseTestCase;
import lib.DataGenerator;
import lib.ApiCoreRequests;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.HashMap;
import java.util.Map;

@Owner("Billing-team")
@TmsLink("Register-tests-link")
@Tag("REGISTER_TESTS")
public class UserRegisterTest extends BaseTestCase {

    private final ApiCoreRequests apiCoreRequests = new ApiCoreRequests();

    @Test
    public void testCreateUserWithExistingEmail(){

        String email = "vinkotov@example.com";

        Map<String, String> userData = new HashMap<>();
        userData.put("email", email);
        userData = DataGenerator.getRegistrationData(userData);

        Response responseCreateAuth = RestAssured
                .given()
                .body(userData)
                .post(Constants.URL + "/user")
                .andReturn();

        Assertions.assertResponseTextEquals(responseCreateAuth, "Users with email '" + email + "' already exists");
        Assertions.assertResponseCodeEquals(responseCreateAuth, 400);
    }

    @Test
    public void testCreateUserSuccessfully(){

        Map<String, String> userData = DataGenerator.getRegistrationData();

        Response responseCreateAuth = RestAssured
                .given()
                .body(userData)
                .post(Constants.URL + "/user")
                .andReturn();

        Assertions.assertResponseCodeEquals(responseCreateAuth, 200);
        Assertions.assertJsonHasField(responseCreateAuth, "id");

    }

    @Test
    public void testIncorrectEmail(){
        Map<String, String> incorrectData = new HashMap<>();
        incorrectData.put("email", "test.example.com");
        incorrectData = DataGenerator.getRegistrationData(incorrectData);

        Response failedResponse = apiCoreRequests.makePostRequest(Constants.URL + "/user", incorrectData);

        Assertions.assertResponseTextEquals(failedResponse, "Invalid email format");

    }

    @ParameterizedTest
    @ValueSource(strings = {"email", "password", "username", "firstName", "lastName"})
    public void absentParametersTest(String parameter){
        Map<String, String> incorrectData = DataGenerator.getRegistrationData();

        incorrectData.remove(parameter);

        Response failedResponse = apiCoreRequests.makePostRequest(Constants.URL + "/user", incorrectData);

        Assertions.assertResponseTextEquals(failedResponse,
                "The following required params are missed: " + parameter);
    }

    @ParameterizedTest
    @ValueSource(strings = {"username", "firstName", "lastName"})
    public void testShortUserData(String userParameter){
        Map<String, String> incorrectData = DataGenerator.getRegistrationData();

        DataGenerator.makeUserDataShort(incorrectData, userParameter);

        Response failedResponse = apiCoreRequests.makePostRequest(Constants.URL + "/user", incorrectData);

        Assertions.assertResponseTextEquals(failedResponse, "The value of '" + userParameter + "' field is too short");

    }

    @ParameterizedTest
    @ValueSource(strings = {"username", "firstName", "lastName"})
    public void testLongUserData(String userParameter){
        Map<String, String> incorrectData = DataGenerator.getRegistrationData();

        DataGenerator.makeUserDataLong(incorrectData, userParameter);

        Response failedResponse = apiCoreRequests.makePostRequest(Constants.URL + "/user", incorrectData);

        Assertions.assertResponseTextEquals(failedResponse, "The value of '" + userParameter + "' field is too long");

    }

}

