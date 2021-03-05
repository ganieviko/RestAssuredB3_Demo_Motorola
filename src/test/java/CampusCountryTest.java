import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.http.Cookies;
import io.restassured.response.ValidatableResponse;
import org.testng.annotations.*;

import java.util.*;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

public class CampusCountryTest {

    private Cookies cookies;
    private List<String> idsForCleanedUp;
    private Map<String, String> body;

    @BeforeClass
    public void setUp() {
        RestAssured.baseURI = "https://test.campus.techno.study";
        body = new HashMap<>();
        body.put("name", "New country " + new Random().nextInt(500));

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

    @BeforeMethod
    public void createCountry() {
        idsForCleanedUp = new ArrayList<>(); // must be emptied
        ValidatableResponse response = given()
                .cookies(cookies)
                .body(body)
                .contentType(ContentType.JSON)
                .when()
                .post("/school-service/api/countries")
                .then().log().body();

        String id = response.statusCode(201).extract().jsonPath().getString("id");
        idsForCleanedUp.add(id);
    }

    @Test()
    public void getCountry() {
        given()
                .cookies(cookies)
                .when()
                .get("/school-service/api/countries/" + idsForCleanedUp.get(0))
                .then()
                .statusCode(200)
                .body("id", equalTo(idsForCleanedUp.get(0)))
                .body("name", equalTo(body.get("name")))
                .body("shortName", equalTo(body.get("shortName")))
        ;
    }

    @Test()
    public void duplicateCountry() {
        given()
                .cookies(cookies)
                .body(body)
                .contentType(ContentType.JSON)
                .when()
                .post("/school-service/api/countries")
                .then()
                .log().body()
                .statusCode(400)
                .body("message", allOf(
                        not(empty()),
                        containsString(body.get("name")),
                        containsString("already exists"))
                );


    }

//    @Test()
//    public void editTest() {
//        given()
//                .cookies(cookies)
//                .body(body)
//                .contentType(ContentType.JSON)
//                .when()
//                .put("/school-service/api/countries")
//    }

    @AfterMethod
    public void cleanup() {
        for (String id : idsForCleanedUp) {
            given()
                    .cookies(cookies)
                    .when()
                    .delete("/school-service/api/countries/" + id)
                    .then()
                    .statusCode(200)
            ;
        }
    }
}
