import configs.browserRules;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.Rule;
import org.openqa.selenium.WebDriver;
import poms.ConstructorPage;
import poms.HomePage;
import io.qameta.allure.Step;
import org.junit.Test;
import io.qameta.allure.Description;

import static org.junit.Assert.assertTrue;

@RunWith(Parameterized.class)
public class ConstructorTabsTestParams {

    @Rule
    public final browserRules browser = new browserRules(); // даёт WebDriver

    private WebDriver driver;
    private HomePage home;
    private ConstructorPage constructor;

    @Before
    public void setUp() {
        driver = browser.getDriver();
        home = new HomePage(driver);
        constructor = new ConstructorPage(driver);
    }


    @Parameterized.Parameters(name = "{index}: переход к разделу «{0}»")
    public static Object[][] data() {
        return new Object[][]{
                {"Булки"},
                {"Соусы"},
                {"Начинки"}
        };
    }
    @Parameterized.Parameter
    public String tabName;

    // ---------- шаги ----------
    @Step("Открываем Главную и ждём «Конструктор»")
    private void openConstructor() {
        home.openHomePage();
        constructor.waitOpenedTab(10);   //
    }

    @Step("Кликаем вкладку «{tabName}»")
    private void clickSection(String tabName) {
        switch (tabName) {
            case "Булки":
                constructor.clickBuns();
                constructor.waitBunsActive(5);
                break;
            case "Соусы":
                constructor.clickSauces();
                constructor.waitSaucesActive(5);
                break;
            case "Начинки":
                constructor.clickFillings();
                constructor.waitFillingsActive(5);
                break;
            default:
                throw new IllegalArgumentException("Неизвестный раздел: " + tabName);
        }
    }

    @Step("Проверяем, что активна вкладка «{tabName}»")
    private void assertSectionActive(String tabName) {
        boolean active;
        switch (tabName) {
            case "Булки":    active = constructor.isBunsActive();     break;
            case "Соусы":    active = constructor.isSaucesActive();   break;
            case "Начинки":  active = constructor.isFillingsActive(); break;
            default: throw new IllegalArgumentException("Неизвестный раздел: " + tabName);

        }
        assertTrue("Ожидали активную вкладку «" + tabName + "»", active);
    }

    // ---------- тест ----------
    @Test
    @Description("Конструктор: переход по вкладкам «Булки/Соусы/Начинки» делает соответствующую вкладку активной")
    public void tabsSwitching_makesSectionActive() {
        openConstructor();
        clickSection(tabName);
        assertSectionActive(tabName);
    }
}
