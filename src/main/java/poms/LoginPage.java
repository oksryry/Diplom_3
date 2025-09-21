package poms;

import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import static org.openqa.selenium.support.ui.ExpectedConditions.visibilityOfElementLocated;

public class LoginPage {
    private final WebDriver driver;
    private final WebDriverWait wait;
    private final By registerLink = By.xpath("//a[text()='Зарегистрироваться']");
    private final By loginPageHeader  = By.xpath("//h2[text()='Вход']");
    private final By loginPageButton  = By.xpath("//button[text()='Войти']");
    private final By inputEmail    = By.xpath("//label[text()='Email']/following::input[1]");
    private final By inputPassword = By.xpath("//label[text()='Пароль']/following::input[1]");
    private final By linkForgot    = By.xpath("//a[text()='Восстановить пароль']");



    public LoginPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, 5);
    }

    public void waitLoaded() { wait.until(visibilityOfElementLocated(loginPageButton)); }

    public void goToRegisterPage() {
        driver.findElement(registerLink).click();
    }

    public void goToForgotPswPage() {driver.findElement(linkForgot).click(); }

    public void fillEmail(String email){ driver.findElement(inputEmail).sendKeys(email); }
    public void fillPassword(String pwd){ driver.findElement(inputPassword).sendKeys(pwd); }
    public void submitLogin(){ driver.findElement(loginPageButton).click(); }


    //Ждём, что страница логина действительно открылась
    public boolean waitLoginPageOpened(int seconds) {
        try {
            new WebDriverWait(driver, seconds).until(
                    ExpectedConditions.or(
                            visibilityOfElementLocated(loginPageHeader),
                            visibilityOfElementLocated(loginPageButton)
                    )
            );
            return true;
        } catch (TimeoutException e) {
            return false;
        }
    }

}
