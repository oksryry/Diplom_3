package uitests;

import configs.ApiRules;
import configs.BrowserRules;
import io.qameta.allure.Description;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.openqa.selenium.WebDriver;
import steps.AuthorizedUserSteps;
import steps.CommonSteps;
import steps.LoginSteps;

public class LoginTests {

    // праваило WebDriver'a
    @Rule
    public final BrowserRules browser = new BrowserRules();

    // RestAssured baseURI правило
    @Rule
    public final ApiRules apiRule = new ApiRules();

    private WebDriver driver;
    private CommonSteps commonSteps;
    private LoginSteps loginSteps;
    private AuthorizedUserSteps authSteps;

    // тестовые данные
    private String email;
    private String password;

    @Before
    public void setUp() {
        driver = browser.getDriver();
        commonSteps = new CommonSteps(driver);
        loginSteps = new LoginSteps(driver);
        authSteps = new AuthorizedUserSteps(driver);

        // Подготовим пользователя через API (для входа в UI)
        authSteps.createUserViaApi();
        email = authSteps.getEmail();
        password = authSteps.getPassword();
    }

    @After
    public void tearDown() {
        authSteps.deleteUserViaApi();
    }


    // ========== ТЕСТЫ ==========

    @Test
    @Description("Вход по кнопке «Войти в аккаунт» на главной")
    public void loginViaMainButtonSuccess() {
        commonSteps.openHome();
        loginSteps.goToLoginFromMainButton();
        loginSteps.loginWith(email, password);
        commonSteps.goToProfileFromHeader();   // чтобы появилась кнопка «Выход»
        commonSteps.assertProfileOpened();
    }

    @Test
    @Description("Вход через кнопку «Личный кабинет»")
    public void loginViaPersonalAccountSuccess() {
        commonSteps.openHome();
        loginSteps.goToLoginFromPersonalAccount();
        loginSteps.loginWith(email, password);
        commonSteps.goToProfileFromHeader();
        commonSteps.assertProfileOpened();
    }

    @Test
    @Description("Вход через кнопку в форме регистрации (ссылка «Войти»)")
    public void loginViaRegisterFormSuccess() {
        commonSteps.openHome();
        loginSteps.goToLoginFromRegisterForm();
        loginSteps.loginWith(email, password);
        commonSteps.goToProfileFromHeader();
        commonSteps.assertProfileOpened();
    }

    @Test
    @Description("Вход через кнопку в форме восстановления пароля (ссылка «Войти»)")
    public void loginViaForgotFormSuccess() {
        commonSteps.openHome();
        loginSteps.goToLoginFromForgotForm();
        loginSteps.loginWith(email, password);
        commonSteps.goToProfileFromHeader();
        commonSteps.assertProfileOpened();
    }
}
