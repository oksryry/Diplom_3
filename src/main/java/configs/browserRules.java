package configs;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.rules.ExternalResource;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

public class browserRules extends ExternalResource {
    private WebDriver driver;

    public WebDriver getDriver() { return driver; }

    @Override
    protected void before() {
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
//        driver = new SafariDriver();
    }

    @Override
    protected void after() {
        driver.quit();
    }

    public WebDriver driver() {
        return driver;
    }
}
