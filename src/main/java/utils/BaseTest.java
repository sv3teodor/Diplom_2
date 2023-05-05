package utils;

import com.github.javafaker.Faker;
import io.restassured.RestAssured;
import org.junit.After;
import org.junit.Before;
import pojo.User;
import pojo.UserSession;

import java.util.Locale;

import static config.BaseConfig.BASE_URL;
import static config.BaseConfig.LOCALE_FOR_TEST;
import static org.apache.http.HttpStatus.SC_OK;
import static utils.UserTestUtils.*;

public abstract class BaseTest {
    public static Faker faker = new Faker(new Locale(LOCALE_FOR_TEST));
    protected User user;
    protected UserSession userSession;

    public BaseTest() {
        user = makeRandomUser();
    }

    @Before
    public void setup() {
        RestAssured.baseURI = BASE_URL;
    }

    public void createDefUser() {
        UserTestUtils.createUser(user).then().statusCode(SC_OK);
    }

    public void loginDefUser() {
        userSession = loginUser(user).as(UserSession.class);
        user = userSession.getUser();
    }

    @After
    public void teardown() {
        if (user != null) {
            cleanUser(user);
        }
    }

}