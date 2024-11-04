import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.notNullValue;

@RunWith(Parameterized.class)
public class CreateOrderTest {
    private final String color;
    private String trackNum;

    public CreateOrderTest(String color) {
        this.color = color;
    }

    @Parameterized.Parameters
    public static Object[][] setColor() {
        return new Object[][] {
                {"BLACK"},
                {"GRAY"},
                {"BLACK,GRAY"},
                {""}
        };
    }
    @Step("Send POST request /api/v1/orders")
    public Response sendPostRequest(String json) {
        return given()
                .header("Content-type", "application/json")
                .body(json)
                .post("/api/v1/orders");
    }

    @Step("get track ID and check Status code and response")
    public String getTrackIdAndCheckStatusCode(Response response) {
        return response
                .then()
                .statusCode(201)
                .body("track",notNullValue())
                .extract()
                .path("track")
                .toString();
    }

    @Before
    public void setUp() {
        RestAssured.baseURI = "https://qa-scooter.praktikum-services.ru/";
    }

    @Test
    @DisplayName("Check creating order with parameters \"color\" /api/v1/orders")
    public void createOrder() {
        final String orderJson = "{\n" +
                "    \"firstName\": \"Naruto\",\n" +
                "    \"lastName\": \"Uchiha\",\n" +
                "    \"address\": \"Konoha, 142 apt.\",\n" +
                "    \"metroStation\": 4,\n" +
                "    \"phone\": \"+7 800 355 35 35\",\n" +
                "    \"rentTime\": 5,\n" +
                "    \"deliveryDate\": \"2020-06-06\",\n" +
                "    \"comment\": \"Saske, come back to Konoha\",\n" +
                "    \"color\": [\n" +
                "        \""+color+"\"\n" +
                "    ]\n" +
                "}";
        Response response = sendPostRequest(orderJson);
        trackNum = getTrackIdAndCheckStatusCode(response);
    }

    @After
    public void delOrder() {
        given()
                .header("Content-type", "application/json")
                .body("{\n" +
                        "    \"track\": "+trackNum+"\n" +
                        "}")
                .put("/api/v1/orders/cancel");

    }
}
