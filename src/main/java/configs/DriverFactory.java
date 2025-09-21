package configs;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

public class DriverFactory {
    private WebDriver driver;


//     * Создаёт и возвращает WebDriver по системному свойству -Dbrowser.
//     * Поддерживаются: "chrome" (по умолчанию) и "firefox".

    public WebDriver initDriver() {
        String browser = System.getProperty("browser", "chrome").toLowerCase().trim();

        switch (browser) {
            case "firefox":
                WebDriverManager.firefoxdriver().setup();
                driver = new FirefoxDriver();
                break;

            case "chrome":
            default:
                WebDriverManager.chromedriver().setup();
                driver = new ChromeDriver();
                break;
        }
        return driver;
    }

    public WebDriver getDriver() {
        return driver;
    }

//    завершает сессию браузера
    public void quit() {
        if (driver != null) {
            try { driver.quit(); } finally { driver = null; }
        }
    }

}
