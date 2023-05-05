import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import pojo.UserSession;
import utils.BaseTest;
import utils.UserTestUtils;

import static config.BaseConfig.PASSWORD_MAX_LENGTH;
import static config.BaseConfig.PASSWORD_MIN_LENGTH;
import static org.apache.http.HttpStatus.SC_OK;
import static org.apache.http.HttpStatus.SC_UNAUTHORIZED;
import static org.junit.Assert.assertTrue;

public class UserLoginTest extends BaseTest {

    @Before
    public void additionalSetup() {
        super.createDefUser();
    }

    @Test
    @DisplayName("User login")
    @Description("логин под существующим пользователем")
    public void loginUser() {
        Response response = UserTestUtils.loginUser(user);
        //Check answer code
        response.then()
                .statusCode(SC_OK)
                .assertThat()
                .body("success", Matchers.is(true));
        //Checking answer json
        assertTrue(response.as(UserSession.class).getAccessToken().length() > 0);
        assertTrue(response.as(UserSession.class).getRefreshToken().length() > 0);

    }

    @Test
    @DisplayName("Trying login with incorrect login")
    @Description("Логин с неверным логином")
    public void loginWithIncorrectLogin() {
        user.setEmail(faker.internet().password(PASSWORD_MIN_LENGTH, PASSWORD_MAX_LENGTH));
        Response response = UserTestUtils.loginUser(user);
        //Check answer code
        response.then()
                .statusCode(SC_UNAUTHORIZED)
                .assertThat()
                .body("success", Matchers.is(false))
                .and()
                .body("message", Matchers.is("email or password are incorrect"))
                .log().all();
    }

    @Test
    @DisplayName("Trying login with incorrect password")
    @Description("Логин с неверным паролем")
    public void loginWithIncorrectPassword() {
        user.setPassword(faker.internet().password(PASSWORD_MIN_LENGTH, PASSWORD_MAX_LENGTH));
        Response response = UserTestUtils.loginUser(user);
        //Check answer code
        response.then()
                .statusCode(SC_UNAUTHORIZED)
                .and()
                .assertThat()
                .body("success", Matchers.is(false))
                .and()
                .assertThat()
                .body("message", Matchers.is("email or password are incorrect"))
                .log().all();
    }
}

