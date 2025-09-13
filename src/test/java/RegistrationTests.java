import com.github.javafaker.Faker;
import configs.browserRules;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.openqa.selenium.WebDriver;
import poms.HomePage;
import poms.LoginPage;
import poms.RegisterPage;
import io.qameta.allure.Step;

import java.util.Locale;

import static org.junit.Assert.assertTrue;

public class RegistrationTests {

    private WebDriver driver;
    private HomePage homePage;
    private LoginPage loginPage;
    private RegisterPage registerPage;

    Faker fakerRU = new Faker(Locale.forLanguageTag("ru"));
    Faker faker = new Faker();

    @Rule
    public final browserRules browserRules = new browserRules();

    @Before
    public void setUp() {
        // JUnit сначала выполнит @Rule, потом сюда зайдёт — драйвер уже должен быть создан
        this.driver = browserRules.getDriver();    // <- возьми из своего правила (или browserRules.driver)
        this.homePage = new HomePage(driver);
        this.loginPage = new LoginPage(driver);
        this.registerPage = new RegisterPage(driver);
    }

    // УСПЕШНАЯ РЕГИСТРАЦИЯ
    @Step("Открываем главную и переходим на форму регистрации")
    private void openRegistrationForm() {
        homePage.openHomePage();
        homePage.clickPersonalAccount();   // экран «Вход»
        loginPage.goToRegisterPage();      // экран «Регистрация»
        registerPage.waitLoaded();
    }

    @Step("Заполняем форму регистрации: name={name}, email={email}")
    private void fillRegistrationForm(String name, String email, String password) {
        registerPage.fillName(name);
        registerPage.fillEmail(email);
        registerPage.fillPassword(password);
    }

    @Step("Отправляем форму регистрации")
    private void submitRegistrationForm() {
        registerPage.submitRegisterForm();
    }

    @Step("Проверяем, что открылся экран 'Вход' (ждём до {seconds} сек.)")
    private void assertRedirectToLogin(int seconds) {
        assertTrue(
                "После регистрации должен открыться экран 'Вход'",
                loginPage.waitLoginPageOpened(seconds)
        );
    }


// ---------- ТЕСТ ----------

    @Test
    public void successfulRegistration_redirectsToLogin() {
        String name = fakerRU.name().firstName();
        String email = faker.internet().safeEmailAddress();
        String pass = faker.internet().password(6, 25);

        openRegistrationForm();
        fillRegistrationForm(name, email, pass);
        submitRegistrationForm();
        assertRedirectToLogin(10);
    }


    // ОШИБКА: КОРОТКИЙ ПАРОЛЬ
    @Step("Заполняем форму регистрации коротким паролем: name={name}, email={email}, password='{password}'")
    private void fillRegistrationFormWithShortPassword(String name, String email, String password) {
        registerPage.fillName(name);
        registerPage.fillEmail(email);
        registerPage.fillPassword(password); // < 6 символов
    }

    @Step("Проверяем, что показана ошибка о некорректном пароле")
    private void assertShortPasswordErrorShown() {
        assertTrue("Должно появиться сообщение о некорректном пароле",
                registerPage.isPasswordErrorShown());
    }


    // ---------- ТЕСТ ----------
    @Test
    public void registrationWithShortPassword_showsError() {
        String name = fakerRU.name().firstName();
        String email = faker.internet().safeEmailAddress();
        String shortPass = faker.internet().password(1, 5);

        openRegistrationForm();
        fillRegistrationFormWithShortPassword(name, email, shortPass);
        submitRegistrationForm();
        assertShortPasswordErrorShown();

    }
}
