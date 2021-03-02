import io.restassured.http.ContentType;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

public class ZippoTest {
    @Test
    public void test() {
        given().when().then();
    }

    @Test
    public void testingStatusCode() {
        given()
                .when()
                .get("http://api.zippopotam.us/us/90210")
                .then()
                .statusCode(200)
        ;
    }

    @Test
    public void loggingRequest() {
        given()
                .log().all()
                .when()
                .get("http://api.zippopotam.us/us/90210")
                .then()
        ;
    }

    @Test
    public void loggingResponse() {
        given()
                .when()
                .get("http://api.zippopotam.us/us/90210")
                .then()
                .log().all()
        ;
    }

    @Test
    public void loggingRequestAndResponse() {
        given()
                .log().all()
                .when()
                .get("http://api.zippopotam.us/us/90210")
                .then()
                .log().all()
        ;
    }

    @Test
    public void testingContentType() {
        given()
                .when()
                .get("http://api.zippopotam.us/us/90210")
                .then()
//        .contentType("application/json")
            .contentType(ContentType.JSON)
        ;
    }

    @Test
    public void checkCountryTest() {
        given()
                .when()
                .get("http://api.zippopotam.us/us/90210")
                .then()
                .body("country", equalTo("United States"))
                .body("'country abbreviation'", equalTo("US"))
                .log().body()
        ;
    }

}
