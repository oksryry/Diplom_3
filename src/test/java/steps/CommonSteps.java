package steps;

import io.qameta.allure.Step;
import org.junit.Assert;
import org.openqa.selenium.WebDriver;
import poms.ConstructorPage;
import poms.HomePage;
import poms.ProfilePage;

//Общие навигационные и проверочные шаги, переиспользуемые в UI-тестах
public class CommonSteps {

    private final HomePage home;
    private final ProfilePage profile;
    private final ConstructorPage constructor;

    public CommonSteps(WebDriver driver) {
        this.home = new HomePage(driver);
        this.profile = new ProfilePage(driver);
        this.constructor = new ConstructorPage(driver);
    }

    // ---------- Навигация ----------

    @Step("Открываем главную страницу")
    public void openHome() {
        home.openHomePage();
    }

    @Step("Переходим в личный кабинет из шапки («Личный кабинет»)")
    public void goToProfileFromHeader() {
        home.clickPersonalAccount();
    }

    @Step("Из личного кабинета переходим в «Конструктор» по ссылке")
    public void goToConstructorViaLinkFromProfile() {
        profile.clickConstructorLink();
    }

    @Step("Из личного кабинета переходим в «Конструктор» по логотипу в шапке")
    public void goToConstructorViaLogoFromProfile() {
        profile.clickHeaderLogo();
    }

    // ---------- Проверки ----------

    @Step("Проверяем, что открыт личный кабинет (видна кнопка «Выход»)")
    public void assertProfileOpened() {
        Assert.assertTrue("Ожидали личный кабинет (кнопка «Выход»)",
                profile.waitAuthorizedState(5));
    }

    @Step("Проверяем, что открыт экран «Конструктор»")
    public void assertConstructorOpened() {
        constructor.waitOpened(10);
    }
}
