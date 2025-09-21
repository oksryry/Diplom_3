package configs;

import org.junit.rules.ExternalResource;
import org.openqa.selenium.WebDriver;

public class BrowserRules extends ExternalResource {
    private WebDriver driver;
    private DriverFactory factory;

    public WebDriver getDriver() { return driver; }

    @Override
    protected void before() {
        factory = new DriverFactory();
        driver = factory.initDriver();
    }

    @Override
    protected void after() {
        if (factory != null) {
            factory.quit();
            factory = null;
            driver = null;
        }
    }
}
