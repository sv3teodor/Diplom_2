package utils;

import config.ApiRequests;
import io.qameta.allure.Step;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import pojo.User;
import pojo.UserSession;

import static config.BaseConfig.PASSWORD_MAX_LENGTH;
import static config.BaseConfig.PASSWORD_MIN_LENGTH;
import static io.restassured.RestAssured.given;
import static org.apache.http.HttpStatus.SC_OK;
import static utils.BaseTest.faker;

public class UserTestUtils {
    @Step("Create user")
    public static Response createUser(User user) {
        return given()
                .contentType(ContentType.JSON)
                .body(user)
                .when()
                .post(ApiRequests.USER_CREATE);
    }

    @Step("User delete")
    public static Response deleteUser(UserSession user) {

        return given()
                .header("Authorization", user.getAccessToken())
                .delete(ApiRequests.USER_DELETE);

    }

    @Step("User login")
    public static Response loginUser(User user) {
        return given()
                .contentType(ContentType.JSON)
                .body(user)
                .post(ApiRequests.USER_LOGIN);
    }

    @Step("User logout")
    public static Response logoutUser(UserSession userSession) {
        String logoutString = "{\n" +
                "\"token\": \"{{refreshToken}}\"\n" +
                "}";
        return given()
                .contentType(ContentType.JSON)
                .body(logoutString.replace("{{refreshToken}}", userSession.getRefreshToken()))
                .post(ApiRequests.USER_LOGOUT);

    }

    @Step("Get user info")
    public static Response getUserInfo(UserSession userSession) {
        return given()
                .header("Authorization", userSession.getAccessToken())
                .get(ApiRequests.USER_GET_INFO);
    }


    @Step("Delete date after tests")
    public static void cleanUser(User user) {
        Response response = UserTestUtils.loginUser(user);
        if (response.statusCode() == SC_OK) {
            UserTestUtils.deleteUser(response.as(UserSession.class));
        }

    }

    @Step("User data changing")
    public static Response editUserData(UserSession userSession) {
        return given()
                .header("Authorization", userSession.getAccessToken())
                .contentType(ContentType.JSON)
                .body(userSession.getUser())
                .patch(ApiRequests.USER_EDIT);
    }

    public static User makeRandomUser() {
        return
                new User(faker.internet().emailAddress(),
                        faker.internet().password(PASSWORD_MIN_LENGTH, PASSWORD_MAX_LENGTH),
                        faker.name().name());
    }

}
