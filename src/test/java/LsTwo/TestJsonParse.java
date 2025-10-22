package LsTwo;

import constants.Constants;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import org.junit.jupiter.api.Test;


public class TestJsonParse {

    @Test
    public void testJsonParse(){

        JsonPath response = RestAssured
                .get(Constants.URL + "/get_json_homework")
                .jsonPath();

        System.out.println(response.getString("messages[1].message"));

    }

}
