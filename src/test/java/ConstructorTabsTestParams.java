import configs.BrowserRules;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import io.qameta.allure.Description;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import utils.CommonSteps;
import utils.ConstructorTabsSteps;


@RunWith(Parameterized.class)
public class ConstructorTabsTestParams {

    @Rule
    public final BrowserRules browser = new BrowserRules(); // даёт WebDriver

    private CommonSteps common;
    private ConstructorTabsSteps tabs;

    // ---------- параметры ----------
    @Parameterized.Parameters(name = "{index}: раздел «{0}»")
    public static Object[] data() {
        return new Object[] { ConstructorTabsSteps.Tab.BUNS, ConstructorTabsSteps.Tab.SAUCES, ConstructorTabsSteps.Tab.FILLINGS };
    }

    @Parameterized.Parameter
    public ConstructorTabsSteps.Tab tab; // toString(): «Булки/Соусы/Начинки»

    @Before
    public void setUp() {
        common = new CommonSteps(browser.getDriver());
        tabs = new ConstructorTabsSteps(browser.getDriver());
    }

    @Test
    public void tabsSwitchingMakesSectionActive() {
        common.openHome();
        common.assertConstructorOpened(); // или open+assert в одном вашем общем шаге
        tabs.clickSection(tab);
        tabs.assertSectionActive(tab);
    }

//    private WebDriver driver;
//    private HomePage home;
//    private ConstructorPage constructor;
//
//    @Before
//    public void setUp() {
//        driver = browser.getDriver();
//        home = new HomePage(driver);
//        constructor = new ConstructorPage(driver);
//    }
//
//
//    @Parameterized.Parameters(name = "{index}: переход к разделу «{0}»")
//    public static Object[][] data() {
//        return new Object[][]{
//                {"Булки"},
//                {"Соусы"},
//                {"Начинки"}
//        };
//    }
//    @Parameterized.Parameter
//    public String tabName;
//
//    // ---------- шаги ----------
//    @Step("Открываем Главную и ждём «Конструктор»")
//    private void openConstructor() {
//        home.openHomePage();
//        constructor.waitOpenedTab(10);   //
//    }
//
//    @Step("Кликаем вкладку «{tabName}»")
//    private void clickSection(String tabName) {
//        switch (tabName) {
//            case "Булки":
//                constructor.clickBuns();
//                constructor.waitBunsActive(5);
//                break;
//            case "Соусы":
//                constructor.clickSauces();
//                constructor.waitSaucesActive(5);
//                break;
//            case "Начинки":
//                constructor.clickFillings();
//                constructor.waitFillingsActive(5);
//                break;
//            default:
//                throw new IllegalArgumentException("Неизвестный раздел: " + tabName);
//        }
//    }
//
//    @Step("Проверяем, что активна вкладка «{tabName}»")
//    private void assertSectionActive(String tabName) {
//        boolean active;
//        switch (tabName) {
//            case "Булки":    active = constructor.isBunsActive();     break;
//            case "Соусы":    active = constructor.isSaucesActive();   break;
//            case "Начинки":  active = constructor.isFillingsActive(); break;
//            default: throw new IllegalArgumentException("Неизвестный раздел: " + tabName);
//
//        }
//        assertTrue("Ожидали активную вкладку «" + tabName + "»", active);
//    }
//
//    // ---------- тест ----------
//    @Test
//    @Description("Конструктор: переход по вкладкам «Булки/Соусы/Начинки» делает соответствующую вкладку активной")
//    public void tabsSwitching_makesSectionActive() {
//        openConstructor();
//        clickSection(tabName);
//        assertSectionActive(tabName);
//    }
}
