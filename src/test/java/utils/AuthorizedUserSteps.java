package utils;

import api.User;
import api.UserCreationAndAuthResponse;
import com.github.javafaker.Faker;
import io.qameta.allure.Step;
import io.restassured.http.ContentType;
import org.junit.Assert;
import org.openqa.selenium.WebDriver;
import poms.ConstructorPage;
import poms.HomePage;
import poms.LoginPage;
import poms.ProfilePage;


import java.util.Locale;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.anyOf;
import static org.hamcrest.Matchers.is;

public class AuthorizedUserSteps {

    private final WebDriver driver;
//    private final CommonSteps common;
    private final HomePage home;
    private final LoginPage login;
    private final ProfilePage profile;
    private final ConstructorPage constructor;

    private final Faker fakerRU = new Faker(Locale.forLanguageTag("ru"));
    private final Faker faker = new Faker();

    // тестовые данные и токен доступны тесту через геттеры
    private String email;
    private String password;
    private String name;
    private String accessToken;

    public AuthorizedUserSteps(WebDriver driver) {
        this.driver = driver;
//        this.common = new CommonSteps(driver);
        this.home = new HomePage(driver);
        this.login = new LoginPage(driver);
        this.profile = new ProfilePage(driver);
        this.constructor = new ConstructorPage(driver);
    }

    public String getEmail() { return email; }
    public String getPassword() { return password; }
    public String getAccessToken() { return accessToken; }


    // ----------------- API STEPS -----------------

    @Step("API: создаём тестового пользователя")
    public void createUserViaApi() {
        name = fakerRU.name().firstName();
        email = faker.internet().safeEmailAddress();
        password = faker.internet().password(6, 25);

        User request = new User(email, password, name);

        UserCreationAndAuthResponse resp =
                given()
                        .contentType(ContentType.JSON)
                        .body(request)
                        .when()
                        .post("/api/auth/register")
                        .then()
                        .statusCode(anyOf(is(200), is(201)))
                        .extract()
                        .as(UserCreationAndAuthResponse.class);

        accessToken = resp.getAccessToken(); // "Bearer ..."
    }

    @Step("API: удаляем пользователя")
    public void deleteUserViaApi() {
        if (accessToken == null) return;

        given()
                .header("Authorization", accessToken)
                .when()
                .delete("/api/auth/user")
                .then()
                .statusCode(anyOf(is(200), is(202), is(204)));
        accessToken = null;
    }



    // ----------------- UI STEPS -----------------

    @Step("UI: логинимся в приложении (через форму «Вход»)")
    public void loginViaUi() {
        home.openHomePage();
        home.clickPersonalAccount(); // попадаем на «Вход»
        login.waitLoaded();
        login.fillEmail(email);
        login.fillPassword(password);
        login.submitLogin();

        // проверим авторизацию (можно в тесте из CommonSteps проверять)
//        home.clickPersonalAccount();
//        Assert.assertTrue("Ожидали авторизованное состояние (кнопка «Выход»)",
//                profile.waitAuthorizedState(5));
    }

    @Step("UI: переходим в Личный кабинет из шапки")
    public void goToProfileFromHeader() {
        home.openHomePage();
        home.clickPersonalAccount();
    }

    @Step("UI: проверяем, что открыт личный кабинет")
    public void assertProfileOpened() {
        Assert.assertTrue("Ожидали личный кабинет (кнопка «Выход»)",
                profile.waitAuthorizedState(5));
    }

    @Step("UI: из личного кабинета кликаем «Конструктор» (переходим по ссылке)")
    public void goToConstructorViaLink() {
        home.openHomePage();
        home.clickPersonalAccount();
        profile.clickConstructorLink();
    }

    @Step("UI: из личного кабинета кликаем «Конструктор» (переходим по логотипу)")
    public void goToConstructorViaLogo() {
        home.openHomePage();
        home.clickPersonalAccount();
        profile.clickHeaderLogo();
    }

    @Step("UI: проверяем, что открыт экран «Конструктор»")
    public void assertConstructorOpened() {
        constructor.waitOpened(10);
    }

}
