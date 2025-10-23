package LsTwo;

import constants.Constants;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

public class LongRedirectTest {

    @Test
    public void testRedirect(){

        String currentLocation = Constants.URL + "/long_redirect";

        while (currentLocation != null) {
            currentLocation = locationGetter(currentLocation);
        }
    }

    private String locationGetter(String targetUrl){
        Response response = RestAssured
                .given()
                .redirects()
                .follow(false)
                .when()
                .get(targetUrl)
                .andReturn();

        int statusCode = response.getStatusCode();
        String location = response.getHeader("Location");

        if (location != null && statusCode != 200) {
            return location;
        } else {
            System.out.println("Location " + targetUrl + " is finally");
            return null;
        }
    }

}
