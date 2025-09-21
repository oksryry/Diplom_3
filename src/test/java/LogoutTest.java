
import configs.ApiRules;
import configs.BrowserRules;;
import io.qameta.allure.Description;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.openqa.selenium.WebDriver;
import utils.AuthorizedUserSteps;
import utils.CommonSteps;
import utils.LogoutSteps;

public class LogoutTest {

    @Rule
    public final BrowserRules browser = new BrowserRules(); // даёт WebDriver
    @Rule
    public final ApiRules apiRule = new ApiRules();               // настраивает RestAssured.baseURI

    private WebDriver driver;
    private CommonSteps commonSteps;
    private AuthorizedUserSteps authSteps;
    private LogoutSteps logoutSteps;

    @Before
    public void setUp() {
        driver = browser.getDriver();
        commonSteps = new CommonSteps(driver);
        authSteps = new AuthorizedUserSteps(driver);
        logoutSteps = new LogoutSteps(driver);

        // Подготовка пользователя через API и авторизация в UI
        authSteps.createUserViaApi();
        authSteps.loginViaUi();
    }

    @After
    public void tearDown() {
        authSteps.deleteUserViaApi();
    }


    // ========== ТЕСТ ==========
    @Test
    @Description("Выход из аккаунта: в личном кабинете нажимаем «Выход» и попадаем на экран логина")
    public void logoutFromProfileOpensLogin() {
        // перейти в ЛК и убедиться, что мы авторизованы
        commonSteps.openHome();
        commonSteps.goToProfileFromHeader();
        commonSteps.assertProfileOpened();

        // выполнить выход и проверить экран логина
        logoutSteps.clickLogout();
        logoutSteps.assertLoginPageOpened(10);
    }
}
