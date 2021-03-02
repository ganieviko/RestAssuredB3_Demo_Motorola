import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;

public class GoRestUsersTest {
    @BeforeClass
    public void setUp() {
        RestAssured.baseURI = "https://gorest.co.in/public-api/users";
    }

    @Test
    public void creatingUser() {
        Map<String, String> body = new HashMap<>();
        body.put("name", "Techno user");
        body.put("email", "Floy_OKon13@yahoo.com");
        body.put("gender", "Female");
        body.put("status", "Active");

        Object id = given()
                .log().uri()
                .header("Authorization", "Bearer 55b19d86844d95532f80c9a2103e1a3af0aea11b96817e6a1861b0d6532eef47")
                .contentType(ContentType.JSON)
                .body(body)
                .when()
                .post()
                .then()
                .log().body()
                .body("code", equalTo(201))
                .extract().path("data.id");

        System.out.println(id);
    }
}
