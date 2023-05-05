import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import org.hamcrest.Matchers;
import org.junit.Test;
import utils.BaseTest;

import static org.apache.http.HttpStatus.SC_FORBIDDEN;
import static org.apache.http.HttpStatus.SC_OK;
import static utils.UserTestUtils.createUser;

public class UserCreateTest extends BaseTest {

    @Test
    @DisplayName("Creating unique user")
    @Description("Создаем уникального пользователя")
    public void createUniqueUser() {
        createUser(user)
                .then()
                .statusCode(SC_OK)
                .assertThat()
                .body("success", Matchers.is(true))
                .and()
                .body("user.email", Matchers.is(user.getEmail()))
                .and()
                .body("user.name", Matchers.is(user.getName()))
                .and()
                .body("accessToken", Matchers.any(String.class))
                .and()
                .body("refreshToken", Matchers.any(String.class))
                .log().all();
    }

    @Test
    @DisplayName("Creating equals users")
    @Description("Создаем пользователя, который уже зарегистрирован")
    public void createEqualsUser() {
        createUser(user)
                .then()
                .statusCode(SC_OK);

        createUser(user)
                .then()
                .statusCode(SC_FORBIDDEN)
                .assertThat()
                .body("success", Matchers.is(false))
                .and()
                .body("message", Matchers.is("User already exists"))
                .log().all();
    }
}
