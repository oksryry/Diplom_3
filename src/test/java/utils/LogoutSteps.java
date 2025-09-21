package utils;

import io.qameta.allure.Step;
import org.junit.Assert;
import org.openqa.selenium.WebDriver;
import poms.LoginPage;
import poms.ProfilePage;

public class LogoutSteps {
    private final ProfilePage profile;
    private final LoginPage login;

    public LogoutSteps(WebDriver driver) {
        this.profile = new ProfilePage(driver);
        this.login = new LoginPage(driver);
    }

    @Step("UI: нажимаем «Выход» в личном кабинете")
    public void clickLogout() {
        profile.clickLogout();
    }

    @Step("UI: проверяем, что открыт экран логина (ждём до {seconds} сек.)")
    public void assertLoginPageOpened(int seconds) {
        Assert.assertTrue("Ожидали экран «Вход» после выхода",
                login.waitLoginPageOpened(seconds));
    }
}
