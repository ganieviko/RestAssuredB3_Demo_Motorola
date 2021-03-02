import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;

public class GoRestUsersTest {
    @BeforeClass
    public void setUp() {
        RestAssured.baseURI = "https://gorest.co.in/public-api/users";
    }

    @Test
    public void creatingUser() {
        Object id = given()
                .log().uri()
                .header("Authorization", "Bearer 55b19d86844d95532f80c9a2103e1a3af0aea11b96817e6a1861b0d6532eef47")
                .contentType(ContentType.JSON)
                .body("{\"name\":\"Techno user\",\"email\":\"Floy_OKon12@yahoo.com\",\"gender\":\"Female\",\"status\":\"Active\"}")
                .when()
                .post()
                .then()
                .log().body()
                .body("code", equalTo(201))
                .extract().path("data.id");

        System.out.println(id);
    }
}
