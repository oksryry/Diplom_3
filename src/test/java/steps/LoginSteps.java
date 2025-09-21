package steps;

import io.qameta.allure.Step;
import org.openqa.selenium.WebDriver;
import poms.ForgotPasswordPage;
import poms.HomePage;
import poms.LoginPage;
import poms.RegisterPage;

public class LoginSteps {

    private final HomePage home;
    private final LoginPage login;
    private final RegisterPage register;
    private final ForgotPasswordPage forgotPsw;

    public LoginSteps(WebDriver driver) {
        this.home = new HomePage(driver);
        this.login = new LoginPage(driver);
        this.register = new RegisterPage(driver);
        this.forgotPsw = new ForgotPasswordPage(driver);
    }

    @Step("Переходим на форму логина: кнопка «Войти в аккаунт» на главной")
    public void goToLoginFromMainButton() {
        home.clickLoginOnMain();
        login.waitLoaded();
    }

    @Step("Переходим на форму логина: через кнопку «Личный кабинет»")
    public void goToLoginFromPersonalAccount() {
        home.clickPersonalAccount();
        login.waitLoaded();
    }

    @Step("Переходим на форму логина: из формы регистрации по ссылке «Войти»")
    public void goToLoginFromRegisterForm() {
        home.clickPersonalAccount();     // «Вход»
        login.goToRegisterPage();        // «Регистрация»
        register.waitLoaded();
        register.clickLoginLink();       // ссылка «Войти»
        login.waitLoaded();
    }

    @Step("Переходим на форму логина: из формы восстановления пароля по ссылке «Войти»")
    public void goToLoginFromForgotForm() {
        home.clickPersonalAccount();     // «Вход»
        login.goToForgotPswPage();       // «Восстановить пароль»
        forgotPsw.waitLoaded();
        forgotPsw.clickLoginLink();      // «Войти»
        login.waitLoaded();
    }

    @Step("Логинимся: email={email}")
    public void loginWith(String email, String pwd) {
        login.fillEmail(email);
        login.fillPassword(pwd);
        login.submitLogin();
    }
}
