import constants.Constants;
import io.restassured.RestAssured;
import io.restassured.http.Headers;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class TestRestAssured {

    @Test
    public void testGetRestAssured(){

        Map<String, String> params = new HashMap<>();
        params.put("name", "tester");

        JsonPath response = RestAssured
                .given()
                .queryParams(params)
                .get(Constants.URL + "/hello")
                .jsonPath();
        String name = response.get("answer");
        System.out.println(Objects.requireNonNullElse(name, "No key"));
    }

    @Test
    public void testPostRestAssured(){

        Map<String, Object> body = new HashMap<>();
        body.put("param1", "value1");
        body.put("param2", "value2");

        Response response = RestAssured
                .given()
                .body(body)
                .post(Constants.URL + "/check_type")
                .andReturn();
        response.print();
    }

    @Test
    public void testStatusCode200(){
        Response response = RestAssured
                .get(Constants.URL + "/check_type")
                .andReturn();

        int statusCode = response.getStatusCode();
        System.out.println(statusCode);
    }

    @Test
    public void testStatusCode500(){
        Response response = RestAssured
                .get(Constants.URL + "/get_500")
                .andReturn();

        int statusCode = response.getStatusCode();
        System.out.println(statusCode);
    }

    @Test
    public void testRedirect(){
        Response response = RestAssured
                .given()
                .redirects()
                .follow(true)
                .when()
                .get(Constants.URL + "/get_303")
                .andReturn();

        int statusCode = response.getStatusCode();
        System.out.println(statusCode);
    }

    @Test
    public void testAllHeaders(){

        Map<String, String> headers = new HashMap<>();
        headers.put("test_header_1", "h1_value");
        headers.put("test_header_2", "h2_value");

        Response response = RestAssured
                .given()
                .headers(headers)
                .when()
                .get(Constants.URL + "/show_all_headers")
                .andReturn();

        response.prettyPrint();

        Headers responseHeaders = response.getHeaders();
        System.out.println(responseHeaders);
    }

    @Test
    public void testLocationHeader(){
        Response response = RestAssured
                .given()
                .redirects()
                .follow(false)
                .when()
                .get(Constants.URL + "/get_303")
                .andReturn();

        response.prettyPrint();

        Headers responseHeaders = response.getHeaders();
        System.out.println("\nResponse headers are:\n" + responseHeaders);

        String locationHeader = response.getHeader("Location");
        System.out.println("\nLocation header is:\n" + locationHeader);
    }

    @Test
    public void testCorrectCookieAndOtherResponseData(){
        Map <String, String> data = new HashMap<>();
        data.put("login", "secret_login");
        data.put("password", "secret_pass");

        Response response = RestAssured
                .given()
                .body(data)
                .when()
                .post(Constants.URL + "/get_auth_cookie")
                .andReturn();

        System.out.println("Pretty:\n");
        response.prettyPrint();

        System.out.println("\nHeaders:\n" + response.getHeaders());

        System.out.println("\nCookies:\n" + response.getCookies());

        System.out.println("\nOnly auth cookie:\n" + response.getCookie("auth_cookie"));
    }

    @Test
    public void testIncorrectCookie(){
        Map <String, String> data = new HashMap<>();
        data.put("login", "secret_login1");
        data.put("password", "secret_pass1");

        Response response = RestAssured
                .given()
                .body(data)
                .when()
                .post(Constants.URL + "/get_auth_cookie")
                .andReturn();

        System.out.println("Pretty:\n");
        response.prettyPrint();

        System.out.println("\nHeaders:\n" + response.getHeaders());

        System.out.println("\nCookies:\n" + response.getCookies());

        System.out.println("\nOnly auth cookie:\n" + response.getCookie("auth_cookie"));
    }

    @Test
    public void testCheckCorrectCookie(){
        String cookieName = "auth_cookie";

        Map <String, String> data = new HashMap<>();
        data.put("login", "secret_login1");
        data.put("password", "secret_pass1");

        Response responseGetCookie = RestAssured
                .given()
                .body(data)
                .when()
                .post(Constants.URL + "/get_auth_cookie")
                .andReturn();

        String responseCookie = responseGetCookie.getCookie(cookieName);

        Map<String, String> cookies = new HashMap<>();
        if (responseCookie != null) {
            cookies.put(cookieName, responseCookie);
        }

        Response responseCheckCookie = RestAssured
                .given()
//                .body(data)
                .cookies(cookies)
                .when()
                .post(Constants.URL + "/check_auth_cookie")
                .andReturn();

        responseCheckCookie.print();
    }
}
