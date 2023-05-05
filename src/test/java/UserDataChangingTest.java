import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import utils.BaseTest;

import static config.BaseConfig.PASSWORD_MAX_LENGTH;
import static config.BaseConfig.PASSWORD_MIN_LENGTH;
import static org.apache.http.HttpStatus.SC_OK;
import static org.apache.http.HttpStatus.SC_UNAUTHORIZED;
import static utils.UserTestUtils.*;

public class UserDataChangingTest extends BaseTest {

    @Before
    public void additionalSetup() {
        super.createDefUser();
        super.loginDefUser();
    }

    @Test
    @DisplayName("Change user email- authorized user")
    @Description("Изменение данных пользователя c авторизацией- меняем email ")
    public void editUserEmail() {
        user.setEmail(faker.internet().emailAddress());
        userSession.setUser(user);
        editUserData(userSession)
                .then()
                .statusCode(SC_OK)
                .and()
                .body("user.email", Matchers.is(user.getEmail()));
    }

    @Test
    @DisplayName("Change user name- authorized user")
    @Description("Изменение данных пользователя c авторизацией- меняем имя")
    public void editUserName() {
        user.setEmail(faker.name().name());
        userSession.setUser(user);
        editUserData(userSession)
                .then()
                .statusCode(SC_OK)
                .and()
                .body("user.name", Matchers.is(user.getName()));
    }

    @Test
    @DisplayName("Change user password- authorized user")
    @Description("Изменение данных пользователя c авторизацией- меняем пароль")
    public void editUserPassword() {

        user.setPassword(faker.internet().password(PASSWORD_MIN_LENGTH, PASSWORD_MAX_LENGTH));
        userSession.setUser(user);
        editUserData(userSession)
                .then()
                .statusCode(SC_OK);
        //Проверяем, что пароль сменился
        //Разлогиниваемся
        logoutUser(userSession).then().statusCode(SC_OK);
        //Логинимся с новым паролем
        loginUser(user).then().statusCode(SC_OK).log().all();
    }

    @Test
    @DisplayName("Edit user email- unauthorized user")
    @Description("Изменение данных пользователя без авторизацией- меняем email ")
    public void editUserEmailUnauthorized() {
        String oldToken = userSession.getAccessToken();
        String oldEmail = userSession.getUser().getEmail();

        userSession.setAccessToken(faker.internet().uuid());
        user.setEmail(faker.internet().emailAddress());
        userSession.setUser(user);
        editUserData(userSession)
                .then()
                .statusCode(SC_UNAUTHORIZED)
                .body("success", Matchers.is(false))
                .and()
                .body("message", Matchers.is("You should be authorised"));
        //Дополнительная проверка, что данные не изменились несмотря на возвращенную ошибку
        userSession.setAccessToken(oldToken);
        getUserInfo(userSession)
                .then()
                .statusCode(SC_OK)
                .body("user.email", Matchers.is(oldEmail));

    }

    @Test
    @DisplayName("Edit user name unauthorized user")
    @Description("Изменение данных пользователя без авторизацией- меняем имя")
    public void editUserNameUnauthorized() {
        String oldToken = userSession.getAccessToken();
        String oldName = userSession.getUser().getName();

        userSession.setAccessToken(faker.internet().uuid());
        user.setEmail(faker.name().name());
        userSession.setUser(user);
        editUserData(userSession)
                .then()
                .statusCode(SC_UNAUTHORIZED)
                .body("success", Matchers.is(false))
                .and()
                .body("message", Matchers.is("You should be authorised"));

        //Дополнительная проверка, что данные не изменились несмотря на возвращенную ошибку
        userSession.setAccessToken(oldToken);
        getUserInfo(userSession)
                .then()
                .statusCode(SC_OK)
                .body("user.name", Matchers.is(oldName));
    }

    @Test
    @DisplayName("Change user password unauthorized user")
    @Description("Изменение данных пользователя без авторизацией- меняем пароль")
    public void editUserPasswordUnauthorized() {

        userSession.setAccessToken(faker.internet().uuid());
        user.setPassword(faker.internet().password(PASSWORD_MIN_LENGTH, PASSWORD_MAX_LENGTH));
        userSession.setUser(user);
        editUserData(userSession)
                .then()
                .statusCode(SC_UNAUTHORIZED)
                .body("success", Matchers.is(false))
                .and()
                .body("message", Matchers.is("You should be authorised"));

    }

}
