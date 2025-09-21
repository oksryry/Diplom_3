package steps;

import io.qameta.allure.Step;
import org.junit.Assert;
import org.openqa.selenium.WebDriver;
import poms.ConstructorPage;

public class ConstructorTabsSteps {

    public enum Tab {
        BUNS("Булки"),
        SAUCES("Соусы"),
        FILLINGS("Начинки");

        private final String title;
        Tab(String title) { this.title = title; }
        @Override public String toString() { return title; }
    }

    private final ConstructorPage constructor;

    public ConstructorTabsSteps(WebDriver driver) {
        this.constructor = new ConstructorPage(driver);
    }

    @Step("Кликаем вкладку «{tab}»")
    public void clickSection(Tab tab) {
        switch (tab) {
            case BUNS :
                constructor.clickBuns();
                constructor.waitBunsActive(5);
                break;
            case SAUCES :
                constructor.clickSauces();
                constructor.waitSaucesActive(5);
                break;
            case FILLINGS :
                constructor.clickFillings();
                constructor.waitFillingsActive(5);
                break;
        }
    }

    @Step("Проверяем, что активна вкладка «{tab}»")
    public void assertSectionActive(Tab tab) {
        boolean active;
        switch (tab) {
            case BUNS:
                active = constructor.isBunsActive();
                break;
            case SAUCES:
                active = constructor.isSaucesActive();
                break;
            case FILLINGS:
                active = constructor.isFillingsActive();
                break;
            default:
                throw new IllegalArgumentException("Неизвестная вкладка: " + tab);
        }
        Assert.assertTrue("Ожидали активную вкладку «" + tab + "»", active);
    }
}
