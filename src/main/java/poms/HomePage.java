package poms;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import static org.openqa.selenium.support.ui.ExpectedConditions.visibilityOfElementLocated;

public class HomePage {

    private WebDriver driver;
    private final WebDriverWait wait;
    private String homePageUrl = "https://stellarburgers.nomoreparties.site";

    //кнопка "Личный кабинет"
    private By personalAccount = By.xpath("//a[normalize-space()='Личный Кабинет' or text()='Личный кабинет']");
    private final By btnLoginOnHomePage     = By.xpath("//button[text()='Войти в аккаунт']");

    public HomePage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, 5);
    }

    public void openHomePage() {
        driver.get(homePageUrl);
    }

    public void clickLoginOnMain() {
        wait.until(visibilityOfElementLocated(btnLoginOnHomePage)).click();
    }

    public void clickPersonalAccount() {
        wait.until(visibilityOfElementLocated(personalAccount)).click();
    }
}
