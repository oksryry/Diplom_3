package uitests;

import configs.BrowserRules;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import io.qameta.allure.Description;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import steps.CommonSteps;
import steps.ConstructorTabsSteps;


@RunWith(Parameterized.class)
public class ConstructorTabsParamsTest {

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
    @Description("Проверяем, что переключение между вкладками конструктора работает")
    public void tabsSwitchingMakesSectionActive() {
        common.openHome();
        common.assertConstructorOpened(); // или open+assert в одном вашем общем шаге
        tabs.clickSection(tab);
        tabs.assertSectionActive(tab);
    }
}
