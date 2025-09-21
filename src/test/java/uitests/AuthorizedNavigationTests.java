package uitests;

import configs.ApiRules;
import configs.BrowserRules;
import io.qameta.allure.Description;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import steps.AuthorizedUserSteps;

public class AuthorizedNavigationTests {

    // Правила проекта (Selenium + RestAssured baseURI)
    @Rule
    public final BrowserRules browser = new BrowserRules();
    @Rule
    public final ApiRules apiRule = new ApiRules();

    private AuthorizedUserSteps steps;


    @Before
    public void setUp() {
        steps = new AuthorizedUserSteps(browser.getDriver());

        // подготовим пользователя через API и залогинимся в UI
        steps.createUserViaApi();
        steps.loginViaUi();
    }

    @After
    public void tearDown() {
        steps.deleteUserViaApi();
    }


    // ------------ ТЕСТЫ ------------

    @Test
    @Description("Переход в личный кабинет — по клику на «Личный кабинет» в шапке")
    public void openProfileFromHeaderSuccess() {
        steps.goToProfileFromHeader();
        steps.assertProfileOpened();
    }

    @Test
    @Description("Переход из личного кабинета в конструктор — по ссылке «Конструктор»")
    public void fromProfileToConstructorViaLinkSuccess() {
        steps.goToConstructorViaLink();
        steps.assertConstructorOpened();
    }

    @Test
    @Description("Переход из личного кабинета в конструктор — по логотипу Stellar Burgers")
    public void fromProfileToConstructorViaLogoSuccess() {
        steps.goToConstructorViaLogo();
        steps.assertConstructorOpened();
    }
}
