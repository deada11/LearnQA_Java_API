package misc.LsTwo;

import constants.Constants;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

public class RedirectTest {

    @Test
    public void testRedirect(){
        Response response = RestAssured
                .given()
                .redirects()
                .follow(false)
                .when()
                .get(Constants.URL + "/long_redirect")
                .andReturn();

        System.out.println(response.getHeader("Location"));
    }
}
