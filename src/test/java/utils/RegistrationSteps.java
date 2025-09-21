package utils;

import io.qameta.allure.Step;
import org.junit.Assert;
import org.openqa.selenium.WebDriver;
import poms.HomePage;
import poms.LoginPage;
import poms.RegisterPage;

public class RegistrationSteps {
    private final HomePage home;
    private final LoginPage login;
    private final RegisterPage register;

    public RegistrationSteps(WebDriver driver) {
        this.home = new HomePage(driver);
        this.login = new LoginPage(driver);
        this.register = new RegisterPage(driver);
    }

    @Step("Переходим на форму регистрации (через «Личный кабинет» → «Регистрация»)")
    public void goToRegistrationForm() {
        home.clickPersonalAccount();   // экран «Вход»
        login.goToRegisterPage();      // экран «Регистрация»
        register.waitLoaded();
    }

    @Step("Заполяем форму регистрации: name={name}, email={email}")
    public void fillRegistrationForm(String name, String email, String password) {
        register.fillName(name);
        register.fillEmail(email);
        register.fillPassword(password);
    }

    @Step("Отправляем форму регистрации")
    public void submitRegistrationForm() {
        register.submitRegisterForm();
    }

    @Step("Проверяем, что выполнен редирект на экран «Вход» (ждём до {seconds} сек.)")
    public void assertRedirectToLogin(int seconds) {
        Assert.assertTrue(
                "После регистрации должен открыться экран «Вход»",
                login.waitLoginPageOpened(seconds)
        );
    }

    // ==== негативные шаги ====

    @Step("Заполняем форму регистрации коротким паролем: name={name}, email={email}, password='{password}'")
    public void fillRegistrationFormWithShortPassword(String name, String email, String password) {
        register.fillName(name);
        register.fillEmail(email);
        register.fillPassword(password); // < 6 символов
    }

    @Step("Проверяем показ ошибки о некорректном пароле")
    public void assertShortPasswordErrorShown() {
        Assert.assertTrue("Должно появиться сообщение о некорректном пароле",
                register.isPasswordErrorShown());
    }
}
