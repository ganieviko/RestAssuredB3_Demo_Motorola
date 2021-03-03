import io.restassured.RestAssured;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import pojo.GoRestUser;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;

public class GoRestUsersTest {

    private RequestSpecification givenReqSpec;
    private ResponseSpecification defaultTestsForResponse;
    private GoRestUser body;
    private Object id;

    @BeforeClass
    public void setUp() {
        RestAssured.baseURI = "https://gorest.co.in/public-api/users";
        givenReqSpec = given()
                .log().uri()
                .header("Authorization", "Bearer 55b19d86844d95532f80c9a2103e1a3af0aea11b96817e6a1861b0d6532eef47")
                .contentType(ContentType.JSON);

        defaultTestsForResponse = given().then().log().body().statusCode(200).contentType(ContentType.JSON);

        body = new GoRestUser();
        body.setName("Techno user");
        body.setEmail("F2loy_"+new Random().nextInt(100)+"OKon15@yahoo.com");
        body.setGender("Female");
        body.setStatus("Active");
    }

    @Test
    public void creatingUser() {
        id = given()
                .spec(givenReqSpec)
                .body(body)
                .when()
                .post()
                .then()
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
                .spec(defaultTestsForResponse)
                .body("code", equalTo(422));
    }

    @Test(dependsOnMethods = "creatingUser")
    public void gettingUser() {
        GoRestUser user = given()
                .when()
                .get("/" + id)
                .then()
                .spec(defaultTestsForResponse)
                .extract()
                .jsonPath().getObject("data", GoRestUser.class);

        Assert.assertEquals(body.getName(), user.getName());
        Assert.assertEquals(body.getEmail(), user.getEmail());
        Assert.assertEquals(body.getGender(), user.getGender());
        Assert.assertEquals(body.getStatus(), user.getStatus());
    }

    @Test(dependsOnMethods = "gettingUser")
    public void updatingUser() {
        // test duplication
        String updatedName = "New Name";
        given()
                .spec(givenReqSpec)
                .body("{\"name\" : \""+updatedName+"\"}")
                .when()
                .put("/" + id)
                .then()
                .spec(defaultTestsForResponse)
                .body("code", equalTo(200))
                .body("data.name", equalTo(updatedName))
        ;
    }

    @Test(dependsOnMethods = "updatingUser")
    public void deletingUser() {
        given()
                .spec(givenReqSpec)
                .when()
                .delete("/" + id)
                .then()
                .spec(defaultTestsForResponse)
                .body("code", equalTo(204));
        ;
    }


}
