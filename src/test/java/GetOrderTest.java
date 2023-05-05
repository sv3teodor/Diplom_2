import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import pojo.OrdersResponse;
import utils.BaseTest;
import utils.OrdersTestUtil;

import static org.apache.http.HttpStatus.SC_OK;
import static org.apache.http.HttpStatus.SC_UNAUTHORIZED;
import static utils.OrdersTestUtil.getAllIngrediens;
import static utils.OrdersTestUtil.getUserOrders;

public class GetOrderTest extends BaseTest {

    @Before
    public void init() {
        super.createDefUser();
        super.loginDefUser();
    }

    @Test
    @DisplayName("Get order- authorized user")
    @Description("Получение заказов конкретного пользователя-авторизованный пользователь")
    public void getOrderAuthorizedUser() {
        OrdersTestUtil.createOrder(userSession, getAllIngrediens());
        Response response = getUserOrders(userSession);
        response.then().statusCode(SC_OK);
        //Проверяем, что вернулся корректный json
        OrdersResponse ordersResponse = response.as(OrdersResponse.class);
    }

    @Test
    @DisplayName("Get order- unauthorized user")
    @Description("Получение заказов конкретного пользователя- не авторизованный пользователь")
    public void getOrderUnauthorizedUser() {
        userSession.setAccessToken(faker.internet().uuid());
        OrdersTestUtil.createOrder(userSession, getAllIngrediens());
        getUserOrders(userSession)
                .then()
                .statusCode(SC_UNAUTHORIZED)
                .body("success", Matchers.is(false))
                .body("message", Matchers.is("You should be authorised"));
    }
}
