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
import poms.ConstructorPage;
import poms.HomePage;
import poms.LoginPage;
import poms.ProfilePage;

import java.util.Locale;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.anyOf;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertTrue;

public class AuthorizedNavigationTests {

    // Правила проекта (Selenium + RestAssured baseURI)
    @Rule
    public final browserRules browser = new browserRules();
    @Rule public final apiRules apiRule = new apiRules();


    private WebDriver driver;
    private HomePage home;
    private LoginPage login;
    private ProfilePage profile;
    private ConstructorPage constructorPage;

    Faker fakerRU = new Faker(Locale.forLanguageTag("ru"));
    Faker faker = new Faker();

    //креды пользака
    private String email;
    private String password;
    private String name;
    private String accessToken; // для удаления пользователя

    @Before
    public void setUp() {
        driver = browser.getDriver();
        home = new HomePage(driver);
        login = new LoginPage(driver);
        profile = new ProfilePage(driver);
        constructorPage = new ConstructorPage(driver);

        // подготовим пользователя через API и залогинимся в UI
        prepareUserViaApi();
        loginViaUi(email, password);
    }

    @After
    public void tearDown() {
        if (accessToken != null) {
            apiDeleteUser(accessToken);
            accessToken = null;
        }
    }

    // ------------ ШАГИ (API) ------------

    @Step("API: создаём тестового пользователя")
    private void prepareUserViaApi() {
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
                        .extract().as(UserCreationAndAuthResponse.class);

        accessToken = resp.getAccessToken(); // "Bearer ..."
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

    // ------------ ШАГИ (UI) ------------

    @Step("UI: логинимся через форму «Вход»")
    private void loginViaUi(String email, String pwd) {
        home.openHomePage();
        home.clickPersonalAccount(); // попадаем на «Вход»
        login.waitLoaded();
        login.fillEmail(email);
        login.fillPassword(pwd);
        login.submitLogin();
        //зайдём в кабинет и убедимся, что авторизованы
        home.clickPersonalAccount();
        assertTrue("Ожидали авторизованное состояние (кнопка «Выход»)",
                profile.waitAuthorizedState(5));
    }

    @Step("UI: кликаем «Личный кабинет» в шапке")
    private void goToProfileFromHeader() {
        home.openHomePage();   // стартуем с главной
        home.clickPersonalAccount();
    }

    @Step("UI: проверяем, что открыт личный кабинет")
    private void assertProfileOpened() {
        assertTrue("Ожидали личный кабинет (кнопка «Выход»)",
                profile.waitAuthorizedState(5));
    }

    @Step("UI: из личного кабинета кликаем «Конструктор»")
    private void goToConstructorViaLink() {
        home.openHomePage();           // вернёмся на главную, затем в кабинет
        home.clickPersonalAccount();   // личный кабинет открыт
        profile.clickConstructorLink();   // «Конструктор»
    }

    @Step("UI: из личного кабинета кликаем по логотипу")
    private void goToConstructorViaLogo() {
        home.openHomePage();
        home.clickPersonalAccount();   // личный кабинет
        profile.clickHeaderLogo();        // логотип
    }

    @Step("UI: проверяем, что открыт «Конструктор»")
    private void assertConstructorOpened() {
                constructorPage.waitOpened(10);

    }


    // ------------ ТЕСТЫ ------------

    @Test
    @Description("Переход в личный кабинет — по клику на «Личный кабинет» в шапке")
    public void openProfileFromHeader_success() {
        goToProfileFromHeader();
        assertProfileOpened();
    }

    @Test
    @Description("Переход из личного кабинета в конструктор — по ссылке «Конструктор»")
    public void fromProfile_toConstructor_viaLink_success() {
        goToConstructorViaLink();
        assertConstructorOpened();
    }

    @Test
    @Description("Переход из личного кабинета в конструктор — по логотипу Stellar Burgers")
    public void fromProfile_toConstructor_viaLogo_success() {
        goToConstructorViaLogo();
        assertConstructorOpened();
    }



}
