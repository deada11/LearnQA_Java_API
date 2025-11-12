package tests;

import constants.Constants;
import io.qameta.allure.*;
import lib.Assertions;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import lib.BaseTestCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import org.junit.jupiter.api.DisplayName;

import lib.ApiCoreRequests;

import static lib.UserActions.loginAsSuperUser;

@Epic("Authorisation cases")
@Feature("Authorisation")
@Owner("Activation-team")
@TmsLink("Authorise-tests-link")
@Tag("AUTHORISE_TESTS")
public class UserAuthTest extends BaseTestCase {

    String cookie;
    String header;
    int userIdOnAuth;
    int statusCode;
    private final ApiCoreRequests apiCoreRequests = new ApiCoreRequests();

    @BeforeEach
    public void loginUser(){
        Response responseGetAuth = loginAsSuperUser();

        this.cookie = this.getCookie(responseGetAuth,Constants.COOKIE_NAME);
        this.header = this.getHeader(responseGetAuth, Constants.TOKEN_NAME);
        this.userIdOnAuth = this.getIntFromJson(responseGetAuth, "user_id");
        this.statusCode = responseGetAuth.getStatusCode();
    }

    @Test
    @Description("Успешная авторизация по почте и паролю")
    @DisplayName("Test positive auth user")
    public void testAuthUser(){

        Response responseCheckAuth = apiCoreRequests
                .makeGetRequest(Constants.URL + "/user/auth",
                        this.header,
                        this.cookie
                );

        Assertions.assertJsonByName(responseCheckAuth, "user_id", this.userIdOnAuth);
    }

    @Description("Проверка авторизации без отправки авторизационной куки и токена")
    @DisplayName("Test negative auth user")
    @ParameterizedTest
    @ValueSource(strings = {"cookie", "headers"})
    public void testNegativeAuthUser(String condition){

        RequestSpecification spec = RestAssured.given();
        spec.baseUri(Constants.URL + "/user/auth");

        if (condition.equals("cookie")){
            Response responseForCheck = apiCoreRequests.makeGetRequestWithCookie(
                    Constants.URL + "/user/auth",
                    this.cookie
            );
            Assertions.assertJsonByName(responseForCheck, "user_id", 0);
        } else if (condition.equals("headers")) {
            Response responseForCheck = apiCoreRequests.makeGetRequestWithToken(Constants.URL + "/user/auth",
                    this.header);
            Assertions.assertJsonByName(responseForCheck, "user_id", 0);
        } else {
            throw new IllegalArgumentException("Unknown condition + " + condition);
        }

    }
}
