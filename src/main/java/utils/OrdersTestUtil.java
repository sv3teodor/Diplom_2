package utils;

import config.ApiRequests;
import io.qameta.allure.Step;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import pojo.Ingredients;
import pojo.UserSession;

import static io.restassured.RestAssured.given;
import static org.apache.http.HttpStatus.SC_OK;

public class OrdersTestUtil {
    @Step("Получить все заказы")
    public static Response getAllOrders() {
        return given()
                .get(ApiRequests.ORDER_GET_ALL);
    }

    @Step("Создать заказ")
    public static Response createOrder(UserSession userSession, Ingredients ingredients) {
        return given()
                .header("Authorization", userSession.getAccessToken())
                .contentType(ContentType.JSON)
                .body(ingredients)
                .when()
                .post(ApiRequests.ORDER_CREATE);
    }

    @Step("Создать заказ")
    public static Response getUserOrders(UserSession userSession) {
        return given()
                .header("Authorization", userSession.getAccessToken())
                .when()
                .get(ApiRequests.ORDER_GET);
    }


    @Step("Получить список хешей ингридиентов")
    public static Ingredients getAllIngrediens() {
        Ingredients ingredients = new Ingredients();
        Response response = given().when().get(ApiRequests.INGREDIENTS_GET_ALL);
        response.then().statusCode(SC_OK);
        ingredients.setIngredients(response.body().jsonPath().getList("data._id", String.class));
        return ingredients;
    }

}
