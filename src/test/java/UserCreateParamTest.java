import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import org.hamcrest.Matchers;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import pojo.User;
import utils.BaseTest;
import utils.UserTestUtils;

import static org.apache.http.HttpStatus.SC_FORBIDDEN;

@RunWith(Parameterized.class)
public class UserCreateParamTest extends BaseTest {
    public UserCreateParamTest(User user) {
        this.user = user;
    }

    @Parameterized.Parameters(name = "Пробуем создать пользователя задав не все параметры. {0}")
    public static Object[][] setTestParam() {
        return new Object[][]{
                {new User("", faker.internet().password(), faker.name().name())},
                {new User(faker.internet().emailAddress(), "", faker.name().name())},
                {new User(faker.internet().emailAddress(), faker.internet().password(), "")}
        };
    }

    @Test
    @DisplayName("Create user")
    @Description("Создаем пользователя без одного из обязательных полей")
    public void createUser() {
        UserTestUtils.createUser(user)
                .then()
                .statusCode(SC_FORBIDDEN)
                .and()
                .assertThat()
                .body("success", Matchers.is(false))
                .and()
                .assertThat()
                .body("message", Matchers.is("Email, password and name are required fields"));
    }

}
