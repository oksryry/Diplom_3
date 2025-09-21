package poms;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import static org.openqa.selenium.support.ui.ExpectedConditions.visibilityOfElementLocated;

public class ForgotPasswordPage {

    private final WebDriver driver;
    private final WebDriverWait wait;

    private final By restoreButton = By.xpath("//button[text()='Восстановить']");
    private final By linkLogin  = By.xpath("//a[text()='Войти']");

    public ForgotPasswordPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, 10);
    }

    public void waitLoaded(){ wait.until(visibilityOfElementLocated(restoreButton)); }
    public void clickLoginLink(){ wait.until(visibilityOfElementLocated(linkLogin)).click(); }

}
