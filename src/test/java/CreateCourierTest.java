import io.qameta.allure.junit4.DisplayName;
import org.junit.After;
import org.junit.Before;
import io.restassured.RestAssured;
import static io.restassured.RestAssured.*;
import org.junit.Test;
import static org.hamcrest.Matchers.equalTo;

public class CreateCourierTest {

    private final String courier = "{\"login\": \"cvbndfvgbh\",\n" +
            "    \"password\": \"1234\",\n" +
            "    \"firstName\": \"saske\"}";

    @Before
    public void setUp() {
        RestAssured.baseURI = "https://qa-scooter.praktikum-services.ru/";
    }

    public String loginId() {
        RestAssured.baseURI = "https://qa-scooter.praktikum-services.ru/";
        String json = "{\"login\": \"cvbndfvgbh\",\n" +
                "    \"password\": \"1234\"}";
        return  given()
                .header("Content-type", "application/json")
                .body(json)
                .post("/api/v1/courier/login")
                .then()
                .extract()
                .path("id")
                .toString();

    }

    @Test
    @DisplayName("Check status code of /api/v1/courier")
    public void CourierCreating() {
        given()
                .header("Content-type", "application/json")
                .body(courier)
                .post("/api/v1/courier")
                .then()
                .statusCode(201)
                .and()
                .assertThat()
                .body("ok", equalTo(true));

    }

    @Test
    @DisplayName("Check to creating dubl courier")
    public void dublikateCourier() {
        given()
                .header("Content-type", "application/json")
                .body(courier)
                .post("/api/v1/courier");
        given()
                .header("Content-type", "application/json")
                .body(courier)
                .post("/api/v1/courier")
                .then()
                .statusCode(409);
    }

    @Test
    @DisplayName("Check create courier Without Login")
    public void createCourierWithoutLogin() {
        String json = "{\"password\": \"1234\",\n" +
                "    \"firstName\": \"saske\"}";
        given()
                .header("Content-type", "application/json")
                .body(json)
                .post("/api/v1/courier")
                .then()
                .statusCode(400);
    }

    @Test
    @DisplayName("Check create courier Without Password")
    public void createCourierWithoutPassword() {
        String json = "{\"login\": \"cvbndfvgbh\",\n" +
                "    \"firstName\": \"saske\"}";
        given()
                .header("Content-type", "application/json")
                .body(json)
                .post("/api/v1/courier")
                .then()
                .statusCode(400);
    }

    @After
    public void delCourier() {
            try {
                String courierId = loginId();
                given()
                        .header("Content-type", "application/json")
                        .delete("/api/v1/courier/" + courierId);
            } catch (NullPointerException exception) {
                return;
            }

    }

}
