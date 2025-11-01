package misc.LsThree;

import constants.Constants;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import lib.BaseTestCase;
import org.junit.jupiter.api.Test;


public class CookieCheckTest extends BaseTestCase {

    @Test
    public void cookieCheckTest() {

        Response response = RestAssured
                .get(Constants.URL + "/homework_cookie")
                .andReturn();

        getCookie(response, "HomeWork");
    }

}
