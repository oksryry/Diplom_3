package poms;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.WebDriverWait;
import static org.openqa.selenium.support.ui.ExpectedConditions.elementToBeClickable;
import static org.openqa.selenium.support.ui.ExpectedConditions.visibilityOfElementLocated;

public class ConstructorPage { //маркеры «Конструктора»

private final WebDriver driver;

    public ConstructorPage(WebDriver driver) { this.driver = driver; }

    // Маркеры конструктора
    private final By h1Title       = By.xpath("//h1[text()='Соберите бургер']");
    private final By btnOrder    = By.xpath("//button[text()='Оформить заказ']");
    private final By tabBunsSpan   = By.xpath("//span[contains(.,'Булки')]");
    private final By tabSaucesSpan = By.xpath("//span[contains(.,'Соусы')]");
    private final By tabFillSpan   = By.xpath("//span[contains(.,'Начинки')]");

    // Кликабельные контейнеры (родитель <span>)
    private final By tabBunsBtn     = By.xpath("//span[contains(.,'Булки')]/ancestor::*[self::button or self::a or contains(@class,'tab')][1]");
    private final By tabSaucesBtn   = By.xpath("//span[contains(.,'Соусы')]/ancestor::*[self::button or self::a or contains(@class,'tab')][1]");
    private final By tabFillingsBtn = By.xpath("//span[contains(.,'Начинки')]/ancestor::*[self::button or self::a or contains(@class,'tab')][1]");

//   Ожидание конструктора
    public void waitOpenedTab(int seconds) {
        WebDriverWait wait = new WebDriverWait(driver, seconds);
        wait.until(visibilityOfElementLocated(h1Title));
        wait.until(org.openqa.selenium.support.ui.ExpectedConditions.or(
                visibilityOfElementLocated(tabBunsSpan),
                visibilityOfElementLocated(tabSaucesSpan),
                visibilityOfElementLocated(tabFillSpan)
        ));
    }

    public void waitOpened(int seconds) {
        WebDriverWait wait = new WebDriverWait(driver, seconds);
        // Последовательное ожидание = логическое И (AND)
        wait.until(visibilityOfElementLocated(h1Title));
        wait.until(visibilityOfElementLocated(btnOrder));
    }

    // Клики по вкладкам с безопасностью
    public void clickBuns()     { safeClick(tabBunsBtn); }
    public void clickSauces()   { safeClick(tabSaucesBtn); }
    public void clickFillings() { safeClick(tabFillingsBtn); }

//    Ждём, пока вкладка станет активной (класс на КОНТЕЙНЕРЕ или aria-selected=true)
    public void waitBunsActive(int seconds)     { waitTabActive(tabBunsBtn, seconds); }
    public void waitSaucesActive(int seconds)   { waitTabActive(tabSaucesBtn, seconds); }
    public void waitFillingsActive(int seconds) { waitTabActive(tabFillingsBtn, seconds); }

//    Быстрая проверка «сейчас активна?» (без ожиданий)
    public boolean isBunsActive()     { return isTabActive(tabBunsBtn); }
    public boolean isSaucesActive()   { return isTabActive(tabSaucesBtn); }
    public boolean isFillingsActive() { return isTabActive(tabFillingsBtn); }

    // ---- приватные помощники ----

    private void safeClick(By by) {
        WebDriverWait wait = new WebDriverWait(driver, 5);
        WebElement el = wait.until(elementToBeClickable(by));
        ((JavascriptExecutor) driver).executeScript(
                "arguments[0].scrollIntoView({block:'center', inline:'nearest'});", el);
        try {
            el.click();
        } catch (ElementClickInterceptedException e) {
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", el);
        }
    }

    private boolean isTabActive(By tabBtn) {
        WebElement el = driver.findElement(tabBtn);
        String cls = java.util.Objects.toString(el.getAttribute("class"), "");
        String aria = java.util.Objects.toString(el.getAttribute("aria-selected"), "");
        return "true".equalsIgnoreCase(aria)
                || cls.contains("current");
    }

    private void waitTabActive(By tabBtn, int seconds) {
        new WebDriverWait(driver, seconds).until(d -> {
            WebElement el = d.findElement(tabBtn);
            String cls = java.util.Objects.toString(el.getAttribute("class"), "");
            String aria = java.util.Objects.toString(el.getAttribute("aria-selected"), "");
            return "true".equalsIgnoreCase(aria)
                    || cls.contains("current");
        });
    }
}
