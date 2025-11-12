package lib;

import constants.Constants;
import io.qameta.allure.Step;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.response.Response;
import java.util.Map;

import static io.restassured.RestAssured.given;

public class ApiCoreRequests {

    @Step("Make a Get-request with token and auth cookie")
    public Response makeGetRequest(String url, String token, String cookie){
        return given()
                .filter(new AllureRestAssured())
                .header(Constants.TOKEN_NAME, token)
                .cookie(Constants.COOKIE_NAME, cookie)
                .get(url)
                .andReturn();
    }

    @Step("Make a Get-request only with auth cookie")
    public Response makeGetRequestWithCookie(String url,  String cookie){
        return given()
                .filter(new AllureRestAssured())
                .cookie(Constants.COOKIE_NAME, cookie)
                .get(url)
                .andReturn();
    }

    @Step("Make a Get-request only with auth token")
    public Response makeGetRequestWithToken(String url,  String token){
        return given()
                .filter(new AllureRestAssured())
                .header(Constants.TOKEN_NAME, token)
                .get(url)
                .andReturn();
    }

    @Step("Make a Post-request")
    public Response makePostRequest(String url, Map<String, String> authData){
        return given()
                .filter(new AllureRestAssured())
                .body(authData)
                .post(url)
                .andReturn();
    }

    @Step("Make a Put-request")
    public Response makePutRequest(String url, String token, String cookie, Map<String, String> dataForChange){
        return given()
                .filter(new AllureRestAssured())
                .header(Constants.TOKEN_NAME, token)
                .cookie(Constants.COOKIE_NAME, cookie)
                .body(dataForChange)
                .put(url)
                .andReturn();
    }

    @Step("Make a Delete-request")
    public Response makeDeleteRequest(String url, String token, String cookie){
        return given()
                .filter(new AllureRestAssured())
                .header(Constants.TOKEN_NAME, token)
                .cookie(Constants.COOKIE_NAME, cookie)
                .delete(url)
                .andReturn();
    }
}
