import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.http.Cookies;
import io.restassured.response.ValidatableResponse;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;

public class CampusCountryTest {

    private Cookies cookies;

    @BeforeClass
    public void setUp() {
        RestAssured.baseURI = "https://test.campus.techno.study";
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
        Map<String, String> country = new HashMap<>();
        country.put("name", "New country 1");

        ValidatableResponse response = given()
                .cookies(cookies)
                .body(country)
                .contentType(ContentType.JSON)
                .when()
                .post("/school-service/api/countries")
                .then();

        response.statusCode(201).log().body();
    }
}
