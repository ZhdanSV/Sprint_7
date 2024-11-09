import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

public class LoginCourierTest {
    private final String courier = "{\n" +
            "    \"login\": \"cvbndfvgbh\",\n" +
            "    \"password\": \"1234\",\n" +
            "    \"firstName\": \"saske\"\n" +
            "}";


    @Before
    public void setUp() {
        RestAssured.baseURI = "https://qa-scooter.praktikum-services.ru/";
        given()
                .header("Content-type", "application/json")
                .body(courier)
                .post("/api/v1/courier");

    }

    @Step("Send Post Request to Login Courier /api/v1/courier/login")
    public Response sendPostRequestLoginCourier(String json) {
        return given()
                .header("Content-type", "application/json")
                .body(json)
                .post("/api/v1/courier/login");
    }

    @Test
    @DisplayName("Check the courier's login with the existing login password pair ")
    public void loginCourier() {
        sendPostRequestLoginCourier(courier)
               .then()
               .statusCode(200)
               .assertThat()
               .body("id",notNullValue());
   }

   @Test
   @DisplayName("Check status code login courier Without Login")
   public void loginCourierWithoutLogin() {
        String json = "{\n" +
                "    \"password\": \"1234\",\n" +
                "    \"firstName\": \"saske\"\n" +
                "}";
       sendPostRequestLoginCourier(json)
               .then()
               .statusCode(400)
               .assertThat()
               .body("message",equalTo("Недостаточно данных для входа"));
   }

   @Test
   @DisplayName("Check status code and body Without Existing Login Password pair")
   public void loginCourierWithNotExistLoginPass() {
       String json = "{\n" +
               "    \"login\": \"pikachu\",\n" +
               "    \"password\": \"0000000000\",\n" +
               "    \"firstName\": \"saske\"\n" +
               "}";
       sendPostRequestLoginCourier(json)
               .then()
               .statusCode(404)
               .assertThat()
               .body("message",equalTo("Учетная запись не найдена"));
   }

    @Test
    @DisplayName("Check status code and body of login courier Without Password")
    public void loginCourierWithoutPassword() {
        String json = "{\n" +
                "    \"login\": \"cvbndfvgbh\",\n" +
                "    \"password\": \"\",\n" +
                "    \"firstName\": \"saske\"\n" +
                "}";
        sendPostRequestLoginCourier(json)
                .then()
                .statusCode(400)
                .assertThat()
                .body("message",equalTo("Недостаточно данных для входа"));
    }


   @After
   public void delCourier() {
        try {
            String courierId = sendPostRequestLoginCourier(courier)
                    .then()
                    .extract()
                    .path("id")
                    .toString();
            given()
                    .header("Content-type", "application/json")
                    .delete("/api/v1/courier/" + courierId);
        } catch (NullPointerException exception) {
               return;
        }
   }

}
