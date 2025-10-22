import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

public class TestClass {

    @Test
    public void getTextTest(){
        Response response = RestAssured
                .get(Constants.URL + "/get_text")
                .andReturn();
        response.prettyPrint();
    }
}
