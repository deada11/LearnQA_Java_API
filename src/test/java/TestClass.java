import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

public class TestClass {

    String URL = " https://playground.learnqa.ru/";

    @Test
    public void getTextTest(){
        Response response = RestAssured
                .get(URL + "api/get_text")
                .andReturn();
        response.prettyPrint();
    }
}
