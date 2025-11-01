package misc;

import constants.Constants;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class JunitUsageTest {

    @Test
    public void testFor200(){
        Response response = RestAssured
                .get(Constants.URL + "/map")
                .andReturn();

        assertEquals(200, response.getStatusCode(), "Unexpected status code");
    }

    @Test
    public void testFor404(){
        Response response = RestAssured
                .get(Constants.URL + "/map2")
                .andReturn();

        assertEquals(404, response.getStatusCode(), "Unexpected status code");
    }

    @Test
    public void testHelloMethodWithoutName(){
        JsonPath response = RestAssured
                .get(Constants.URL + "/hello")
                .jsonPath();
        String answer = response.getString("answer");

        assertEquals("Hello, someone", answer, "There is no name");
    }

    @Test
    public void testHelloMethodWithName(){
        String name = "Username";
        JsonPath response = RestAssured
                .given()
                .queryParam("name", name)
                .get(Constants.URL + "/hello")
                .jsonPath();
        String answer = response.getString("answer");

        assertEquals("Hello, " + name, answer, "There is no name");
    }

    @ParameterizedTest
    @ValueSource(strings = {"", "Tester", "Pester"})
    public void testHelloMethodParametrizedTest(String name){
        Map<String, String> queryParams = new HashMap<>();

        if (!name.isEmpty()){
            queryParams.put("name", name);
        }

        JsonPath response = RestAssured
                .given()
                .queryParams(queryParams)
                .get(Constants.URL + "/hello")
                .jsonPath();
        String answer = response.getString("answer");
        String expectedName = (!name.isEmpty()) ? name : "someone";
        assertEquals("Hello, " + expectedName, answer, "Incorrect name");
    }

}
