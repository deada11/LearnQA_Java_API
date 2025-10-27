package LsTwo;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

public class TokensTest {

    private final static String TASK_LOCATION = "https://playground.learnqa.ru/ajax/api/longtime_job";

    @Test
    public void testTokenRequest() throws InterruptedException {

        JsonPath createTask = RestAssured
                .get(TASK_LOCATION)
                .jsonPath();
        String token = createTask.getJsonObject("token");

        Map<String, String> queryParameters = new HashMap<>();
        queryParameters.put("token", token);

        JsonPath firstStatusCheck = RestAssured
                .given()
                .queryParams(queryParameters)
                .get(TASK_LOCATION)
                .jsonPath();

        if (firstStatusCheck.getJsonObject("status").equals("Job is NOT ready")) {
            System.out.println("Job is not ready, correct status");
        }

        Thread.sleep(createTask.getInt("seconds") * 1000L);

        JsonPath secondStatusCheck = RestAssured
                .given()
                .queryParams(queryParameters)
                .get(TASK_LOCATION)
                .jsonPath();

        if (secondStatusCheck.getJsonObject("status").equals("Job is ready")){
            System.out.println("Job is ready. Status is: " + secondStatusCheck.getJsonObject("result").toString());
        }

    }

}