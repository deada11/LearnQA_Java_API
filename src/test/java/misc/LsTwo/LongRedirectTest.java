package misc.LsTwo;

import constants.Constants;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

public class LongRedirectTest {

    @Test
    public void testRedirect(){

        String currentLocation = Constants.URL + "/long_redirect";
        int redirectCount = 0;

        while (currentLocation != null){
            currentLocation = locationGetter(currentLocation);
            ++redirectCount;
        }
        System.out.println("\nCount of redirects: " + (redirectCount - 1));
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
