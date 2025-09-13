package poms;

import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import static org.openqa.selenium.support.ui.ExpectedConditions.visibilityOfElementLocated;

public class ProfilePage {
    private final WebDriver driver;
    private final WebDriverWait wait;

    private final By btnLogout = By.xpath("//button[text()='Выход']");
    private final By linkConstructorP = By.xpath("//p[text()='Конструктор']");
    private final By headerLogoLink = By.xpath("//div[@class='AppHeader_header__logo__2D0X2']");
    public ProfilePage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, 5);
    }

    public boolean waitAuthorizedState(int seconds) {
        try {
            new WebDriverWait(driver, seconds).until(visibilityOfElementLocated(btnLogout));
            return true;
        } catch (TimeoutException e) { return false; }
    }

    public void clickConstructorLink() {
        driver.findElement(linkConstructorP).click();
    }

    public void clickHeaderLogo() {
        wait.until(visibilityOfElementLocated(headerLogoLink)).click();
    }

    //Кликаем «Выход»
    public void clickLogout() {
        driver.findElement(btnLogout).click();
    }
}
