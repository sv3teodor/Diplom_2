import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import pojo.Ingredients;
import utils.BaseTest;
import utils.OrdersTestUtil;

import java.util.List;

import static org.apache.http.HttpStatus.*;
import static utils.OrdersTestUtil.getAllIngrediens;

public class OrdersCreateTest extends BaseTest {
    private Ingredients ingredients;

    @Before
    public void init() {
        super.createDefUser();
        super.loginDefUser();
        ingredients = new Ingredients();
    }

    @Test
    @DisplayName("Creating order")
    @Description("Создаем заказ под авторизованным пользователем.")
    public void createOrder() {
        OrdersTestUtil.createOrder(userSession, getAllIngrediens())
                .then()
                .statusCode(SC_OK)
                .body("success", Matchers.is(true))
                .and()
                .body("name", Matchers.any(String.class))
                .and()
                .body("order.number", Matchers.any(Integer.class))
                .log().all();
    }

    @Test
    @DisplayName("Create unauthorized order ")
    @Description("Создаем заказ под не авторизованным пользователем")
    public void createOrderUnauthorized() {
        userSession.setAccessToken(faker.internet().uuid());//Меняем токен пользователя
        OrdersTestUtil.createOrder(userSession, getAllIngrediens())
                .then()
                .log()
                .all()
                //Тут баг- приходит 200 код и усеченный json c заказом, но без компонентов.
                //Хотя ошибается отказ
                .statusCode(SC_UNAUTHORIZED)
                .body("success", Matchers.is(false));

    }

    @Test
    @DisplayName("Create order without ingredients")
    @Description("Создаем заказ с авторизацией, но без ингредиентов")
    public void createOrderWithOutIngredients() {
        ingredients.setIngredients(null);
        OrdersTestUtil.createOrder(userSession, ingredients)
                .then()
                .statusCode(SC_BAD_REQUEST)
                .body("success", Matchers.is(false))
                .body("message", Matchers.is("Ingredient ids must be provided"));
    }

    @Test
    @DisplayName("Create order with fake ingredients")
    @Description("Создать заказ с авторизацией с неверным хешем ингредиентов")
    public void createOrderWithFakeIngredients() {
        ingredients.setIngredients(List.of(faker.internet().uuid(), faker.internet().uuid()));
        OrdersTestUtil.createOrder(userSession, ingredients)
                .then()
                .log()
                .body()
                .statusCode(SC_INTERNAL_SERVER_ERROR);
    }
}
