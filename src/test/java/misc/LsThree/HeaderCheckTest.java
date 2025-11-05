package misc.LsThree;

import constants.Constants;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import lib.BaseTestCase;
import org.junit.jupiter.api.Test;


public class HeaderCheckTest extends BaseTestCase {

    @Test
    public void headerCheckTest() {

        Response response = RestAssured
                .get(Constants.URL + "/homework_header")
                .andReturn();

        getHeader(response, "x-secret-homework-header");
    }
}
