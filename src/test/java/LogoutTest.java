import com.github.javafaker.Faker;
import configs.apiRules;
import configs.browserRules;
import forApi.User;
import forApi.UserCreationAndAuthResponse;
import io.qameta.allure.Description;
import io.qameta.allure.Step;
import io.restassured.http.ContentType;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.openqa.selenium.WebDriver;
import poms.*;

import java.util.Locale;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.anyOf;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertTrue;


public class LogoutTest {

    @Rule
    public final browserRules browser = new browserRules(); // даёт WebDriver
    @Rule
    public final apiRules apiRule = new apiRules();               // настраивает RestAssured.baseURI

    private WebDriver driver;
    private HomePage home;
    private LoginPage login;
    private ProfilePage profile;

    Faker fakerRU = new Faker(Locale.forLanguageTag("ru"));
    Faker faker = new Faker();

    // тестовые данные
    private String email;
    private String password;
    private String name;
    private String accessToken; // "Bearer ..." — для очистки пользователя через API

    @Before
    public void setUp() {
        driver  = browser.getDriver();
        home    = new HomePage(driver);
        login   = new LoginPage(driver);
        profile = new ProfilePage(driver);

        prepareUserViaApi();         // создали пользователя
        loginViaUi(email, password); // авторизовались в UI
    }

    @After
    public void tearDown() {
        if (accessToken != null) {
            apiDeleteUser(accessToken);
            accessToken = null;
        }
    }


    // ========== ШАГИ (UI) ==========

    @Step("UI: логинимся через форму «Вход»")
    private void loginViaUi(String email, String pwd) {
        home.openHomePage();
        home.clickPersonalAccount(); // экран «Вход»
        login.waitLoaded();
        login.fillEmail(email);
        login.fillPassword(pwd);
        login.submitLogin();
    }

    @Step("UI: открываем личный кабинет из шапки")
    private void openProfileFromHeader() {
        home.openHomePage();
        home.clickPersonalAccount();
        profile.waitAuthorizedState(5);
    }

    @Step("UI: нажимаем «Выход»")
    private void clickLogout() {
        profile.clickLogout();
    }

    @Step("UI: проверяем, что открыт экран логина (ждём до {seconds} сек.)")
    private void assertLoginPageOpened(int seconds) {
        assertTrue("Ожидали экран 'Вход' после выхода",
                login.waitLoginPageOpened(seconds));
    }

    // ========== ШАГИ (API) ==========

    @Step("API: создаём тестового пользователя")
    private void prepareUserViaApi() {
        name = fakerRU.name().firstName();
        email = faker.internet().safeEmailAddress();
        password = faker.internet().password(6, 25);

        UserCreationAndAuthResponse response =
                given()
                        .contentType(ContentType.JSON)
                        .body(new User(email, password, name))
                        .when()
                        .post("/api/auth/register")
                        .then()
                        .statusCode(anyOf(is(200), is(201)))
                        .extract().as(UserCreationAndAuthResponse.class);

        accessToken = response.getAccessToken(); // пригодится для удаления в @After
    }

    @Step("API: удаляем пользователя")
    private void apiDeleteUser(String token) {
        given()
                .header("Authorization", token)
                .when()
                .delete("/api/auth/user")
                .then()
                .statusCode(anyOf(is(200), is(202), is(204)));
    }

    // ========== ТЕСТ ==========
    @Test
    @Description("Выход из аккаунта: в личном кабинете нажимаем «Выход» и попадаем на экран логина")
    public void logout_fromProfile_opensLogin() {
        openProfileFromHeader();
        clickLogout();
        assertLoginPageOpened(10);
    }
}
