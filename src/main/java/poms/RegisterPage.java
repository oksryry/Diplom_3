package poms;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import static org.openqa.selenium.support.ui.ExpectedConditions.visibilityOfElementLocated;

public class RegisterPage {

    private final WebDriver driver;
    private final WebDriverWait wait;

    private final By nameInput     = By.xpath("//label[text()='Имя']/following::input[1]");
    private final By emailInput    = By.xpath("//label[text()='Email']/following::input[1]");
    private final By passwordInput = By.xpath("//label[text()='Пароль']/following::input[1]");
    private final By submitButton  = By.xpath("//button[text()='Зарегистрироваться']");
    private final By passwordError = By.xpath("//*[contains(.,'Некорректный пароль') or contains(.,'6')]");
    private final By loginButton = By.xpath("//a[text()='Войти']");

    public RegisterPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, 10);
    }

    public void fillName(String name)     { driver.findElement(nameInput).sendKeys(name); }
    public void fillEmail(String email)   { driver.findElement(emailInput).sendKeys(email); }
    public void fillPassword(String pass) { driver.findElement(passwordInput).sendKeys(pass); }
    public void submitRegisterForm() { driver.findElement(submitButton).click(); }
    public void clickLoginLink(){ driver.findElement(loginButton).click(); }

    public void waitLoaded() {
        wait.until(visibilityOfElementLocated(submitButton));
    }

    public boolean isPasswordErrorShown() {
        return !driver.findElements(passwordError).isEmpty();
    }
}
