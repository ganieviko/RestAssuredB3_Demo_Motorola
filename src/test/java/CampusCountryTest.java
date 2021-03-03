import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.http.Cookies;
import io.restassured.response.ValidatableResponse;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.empty;

public class CampusCountryTest {

    private Cookies cookies;
    private String id;
    private Map<String, String> country;

    @BeforeClass
    public void setUp() {
        RestAssured.baseURI = "https://test.campus.techno.study";
        country = new HashMap<>();
        country.put("name", "New country " + new Random().nextInt(500));
    }

    @Test
    public void login() {
        Map<String, String> credentials = new HashMap<>();
        credentials.put("username", "daulet2030@gmail.com");
        credentials.put("password", "TechnoStudy123@");
        ValidatableResponse response = given()
                .body(credentials)
                .contentType(ContentType.JSON)
                .when()
                .post("/auth/login")
                .then();

        response.statusCode(200);

        cookies = response.extract().detailedCookies();
    }

    @Test(dependsOnMethods = "login")
    public void createCountry() {
        ValidatableResponse response = given()
                .cookies(cookies)
                .body(country)
                .contentType(ContentType.JSON)
                .when()
                .post("/school-service/api/countries")
                .then();

        id = response.statusCode(201).extract().jsonPath().getString("id");
    }

    @Test(dependsOnMethods = "createCountry")
    public void duplicateCountry() {
        given()
                .cookies(cookies)
                .body(country)
                .contentType(ContentType.JSON)
                .when()
                .post("/school-service/api/countries")
                .then()
                .log().body()
                .statusCode(400)
//                .body("message", equalTo("The Country with Name \""+country.get("name") +"\" already exists."))
                .body("message", allOf(
                        not(empty()),
                        containsString(country.get("name")),
                        containsString("already exists"))
                );


    }

    @AfterClass
    public void cleanup() {
        given()
                .cookies(cookies)
                .when()
                .delete("/school-service/api/countries/" + id)
                .then()
                .statusCode(200)
        ;
    }
}
