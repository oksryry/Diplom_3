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

public class LoginTests {

    // праваило WebDriver'a
    @Rule
    public final browserRules browser = new browserRules();

    // RestAssured baseURI правило
    @Rule
    public final apiRules apiRule = new apiRules();

    private WebDriver driver;
    private HomePage home;
    private LoginPage login;
    private RegisterPage register;
    private ForgotPasswordPage forgotPsw;
    private ProfilePage profile;

    private final Faker fakerRU = new Faker(Locale.forLanguageTag("ru"));
    Faker faker = new Faker();

    // тестовые данные
    private String email;
    private String password;
    private String name;
    private String accessToken; // для зачистки

    @Before
    public void setUp() {
        driver = browser.getDriver();
        home    = new HomePage(driver);
        login   = new LoginPage(driver);
        register= new RegisterPage(driver);
        forgotPsw  = new ForgotPasswordPage(driver);
        profile = new ProfilePage(driver);

        // Готовим пользователя через API (будем логиниться им в UI)
        prepareUserViaApi();
    }

    @After
    public void tearDown() {
        if (accessToken != null) {
            apiDeleteUser(accessToken);
            accessToken = null;
        }
    }


    // ========== ШАГИ (UI) ==========

    @Step("Открываем главную страницу")
    private void openHome() { home.openHomePage(); }

    @Step("Переходим на форму логина: кнопка «Войти в аккаунт» на главной")
    private void goToLoginFromMainButton() { home.clickLoginOnMain(); login.waitLoaded(); }

    @Step("Переходим на форму логина: через кнопку «Личный кабинет»")
    private void goToLoginFromPersonalAccount() { home.clickPersonalAccount(); login.waitLoaded(); }

    @Step("Переходим на форму логина: из формы регистрации по ссылке «Войти»")
    private void goToLoginFromRegisterForm() {
        home.clickPersonalAccount();      // попадаем на «Вход»
        login.goToRegisterPage();         // «Регистрация»
        register.waitLoaded();
        register.clickLoginLink();        // ссылка «Войти»
        login.waitLoaded();
    }

    @Step("Переходим на форму логина: из формы восстановления пароля по ссылке «Войти»")
    private void goToLoginFromForgotForm() {
        home.clickPersonalAccount();      // «Вход»
        login.goToForgotPswPage();           // «Восстановить пароль»
        forgotPsw.waitLoaded();
        forgotPsw.clickLoginLink();          // «Войти»
        login.waitLoaded();
    }

    @Step("Логинимся: email={email}")
    private void loginWith(String email, String pwd) {
        login.fillEmail(email);
        login.fillPassword(pwd);
        login.submitLogin();
    }
    @Step("Проверяем, что пользователь авторизован (видна кнопка «Выход» в профиле)")
    private void shouldBeAuthorized() {
        // Сам переход в «Личный кабинет» часто обязателен, чтобы появился «Выход»
        home.clickPersonalAccount();
        assertTrue("Ожидали авторизованное состояние (кнопка «Выход»)",
                profile.waitAuthorizedState(5));
    }


    // ========== ШАГИ (API) ==========

    @Step("API: создаём тестового пользователя")
    private void prepareUserViaApi() {
        name = fakerRU.name().firstName();
        email = faker.internet().safeEmailAddress();
        password = faker.internet().password(6, 25);

        accessToken = apiCreateUser(email, password, name); // "Bearer ..."
    }

    @Step("API: регистрируем пользователя {email}")
    private String apiCreateUser(String email, String password, String name) {
        // для запроса на реигстрацию используем класс User (email, password, name)
        User request = new User(email, password, name);

        // для ответа на запрос о регистрации используем класс UserCreationAndAuthResponse
        UserCreationAndAuthResponse response =
                given()
                        .contentType(ContentType.JSON)
                        .body(request)
                        .when()
                        .post("/api/auth/register")
                        .then()
                        .statusCode(anyOf(is(200), is(201)))
                        .extract()
                        .as(UserCreationAndAuthResponse.class);

        return response.getAccessToken();
    }

    private void apiDeleteUser(String accessToken) {
        io.restassured.RestAssured.given()
                .header("Authorization", accessToken)
                .when()
                .delete("/api/auth/user")
                .then()
                .statusCode(org.hamcrest.Matchers.anyOf(
                        is(200), is(202), is(204)));
    }


    // ========== ТЕСТЫ ==========

    @Test
    @Description("Вход по кнопке «Войти в аккаунт» на главной")
    public void login_viaMainButton_success() {
        openHome();
        goToLoginFromMainButton();
        loginWith(email, password);
        shouldBeAuthorized();
    }

    @Test
    @Description("Вход через кнопку «Личный кабинет»")
    public void login_viaPersonalAccount_success() {
        openHome();
        goToLoginFromPersonalAccount();
        loginWith(email, password);
        shouldBeAuthorized();
    }

    @Test
    @Description("Вход через кнопку в форме регистрации (ссылка «Войти»)")
    public void login_viaRegisterForm_success() {
        openHome();
        goToLoginFromRegisterForm();
        loginWith(email, password);
        shouldBeAuthorized();
    }

    @Test
    @Description("Вход через кнопку в форме восстановления пароля (ссылка «Войти»)")
    public void login_viaForgotForm_success() {
        openHome();
        goToLoginFromForgotForm();
        loginWith(email, password);
        shouldBeAuthorized();
    }


}
