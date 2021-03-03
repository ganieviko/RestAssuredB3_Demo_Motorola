import io.restassured.RestAssured;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
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
        body.put("email", "Floy_OKon15@yahoo.com");
        body.put("gender", "Female");
        body.put("status", "Active");


        RequestSpecification givenReqSpec = given()
                .log().uri()
                .header("Authorization", "Bearer 55b19d86844d95532f80c9a2103e1a3af0aea11b96817e6a1861b0d6532eef47")
                .contentType(ContentType.JSON);

        ResponseSpecification defaultTestsForResponse = new ResponseSpecBuilder()
                .expectStatusCode(200)
                .expectContentType(ContentType.JSON)
                .build();

        Object id = given()
                .spec(givenReqSpec)
                .body(body)
                .when()
                .post()
                .then()
                .log().body()
                .spec(defaultTestsForResponse)
                .body("code", equalTo(201))
                .extract().path("data.id");

        // test duplication
        given()
                .spec(givenReqSpec)
                .body(body)
                .when()
                .post()
                .then()
                .log().body()
                .spec(defaultTestsForResponse)
                .body("code", equalTo(422));

        given()
                .when()
                .get("/" + id)
                .then()
                .log().body()
                .spec(defaultTestsForResponse)
                .body("data.name", equalTo(body.get("name")))
                .body("data.email", equalTo(body.get("email")))
                .body("data.gender", equalTo(body.get("gender")))
                .body("data.status", equalTo(body.get("status")))
        ;
    }
}
