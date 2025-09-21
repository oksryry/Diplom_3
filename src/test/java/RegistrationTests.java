import com.github.javafaker.Faker;
import configs.BrowserRules;
import io.qameta.allure.Description;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.openqa.selenium.WebDriver;
import utils.AuthorizedUserSteps;
import utils.CommonSteps;
import utils.RegistrationSteps;
import java.util.Locale;


public class RegistrationTests {

    private WebDriver driver;
    private CommonSteps commonSteps;
    private RegistrationSteps registration;

    private AuthorizedUserSteps authSteps;

    Faker fakerRU = new Faker(Locale.forLanguageTag("ru"));
    Faker faker = new Faker();

    @Rule
    public final BrowserRules browserRules = new BrowserRules();

    @Before
    public void setUp() {
        // JUnit сначала выполнит @Rule, потом сюда зайдёт — драйвер уже должен быть создан
        driver = browserRules.getDriver();
        commonSteps = new CommonSteps(driver);
        registration = new RegistrationSteps(driver);
        authSteps = new AuthorizedUserSteps(driver);
    }

    @After
    public void tearDown() {
        if (authSteps != null) {
            authSteps.deleteUserViaApi();
        }
    }


// ---------- ТЕСТ ----------

    @Test
    @Description("Успешная регистрация: после отправки формы происходит редирект на экран «Вход»")
    public void successfulRegistrationRedirectsToLogin() {
        String name = fakerRU.name().firstName();
        String email = faker.internet().safeEmailAddress();
        String pass = faker.internet().password(6, 25);

        commonSteps.openHome();
        registration.goToRegistrationForm();
        registration.fillRegistrationForm(name, email, pass);
        registration.submitRegistrationForm();
        registration.assertRedirectToLogin(10);
    }

    // ---------- ТЕСТ ----------
    @Test
    @Description("Регистрация с коротким паролем: показывается ошибка о некорректном пароле")
    public void registrationWithShortPasswordShowsError() {
        String name = fakerRU.name().firstName();
        String email = faker.internet().safeEmailAddress();
        String shortPass = faker.internet().password(1, 5);

        commonSteps.openHome();
        registration.goToRegistrationForm();
        registration.fillRegistrationFormWithShortPassword(name, email, shortPass);
        registration.submitRegistrationForm();
        registration.assertShortPasswordErrorShown();

    }
}
