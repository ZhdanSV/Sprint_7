import io.qameta.allure.junit4.DisplayName;
import org.junit.Before;
import io.restassured.RestAssured;
import static io.restassured.RestAssured.*;
import org.junit.Test;
import static org.hamcrest.Matchers.notNullValue;

public class GetOrdersListTest {

    @Before
    public void setUp() {
        RestAssured.baseURI = "https://qa-scooter.praktikum-services.ru/";
    }

    @Test
    @DisplayName("Check status code and not null body of /api/v1/orders")
    public void getOrderList() {
        given()
                .get("/api/v1/orders")
                .then()
                .statusCode(200)
                .body("orders", notNullValue());
    }

}
