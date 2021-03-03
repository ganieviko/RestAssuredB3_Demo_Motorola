import io.restassured.RestAssured;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;

public class GoRestUsersTest {

    private RequestSpecification givenReqSpec;
    private ResponseSpecification defaultTestsForResponse;
    private Map<String, String> body;
    private Object id;

    @BeforeClass
    public void setUp() {
        RestAssured.baseURI = "https://gorest.co.in/public-api/users";
        givenReqSpec = given()
                .log().uri()
                .header("Authorization", "Bearer 55b19d86844d95532f80c9a2103e1a3af0aea11b96817e6a1861b0d6532eef47")
                .contentType(ContentType.JSON);

        defaultTestsForResponse = given().then().statusCode(200).contentType(ContentType.JSON);

        body = new HashMap<>();
        body.put("name", "Techno user");
        body.put("email", "F2loy_"+new Random().nextInt(100)+"OKon15@yahoo.com");
        body.put("gender", "Female");
        body.put("status", "Active");
    }

    @Test
    public void creatingUser() {
        id = given()
                .spec(givenReqSpec)
                .body(body)
                .when()
                .post()
                .then()
                .log().body()
                .spec(defaultTestsForResponse)
                .body("code", equalTo(201))
                .extract().path("data.id");
    }

    @Test(dependsOnMethods = "creatingUser")
    public void creatingDuplicateUser() {
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
    }

    @Test(dependsOnMethods = "creatingUser")
    public void gettingUser() {
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

    @Test(dependsOnMethods = "gettingUser")
    public void deletingUser() {
        given()
                .spec(givenReqSpec)
                .when()
                .delete("/" + id)
                .then()
                .log().body()
                .spec(defaultTestsForResponse)
                .body("code", equalTo(204));
        ;
    }


}
