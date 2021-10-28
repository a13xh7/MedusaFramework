import app.App;
import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.WebDriverRunner;
import medusa.Medusa;
import medusa.Report;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;

public class A_BaseTest {

    protected App app;

    @BeforeClass
    public void setUp() {
        Medusa.init();
        initBrowser();
        app = new App();
    }

    @AfterMethod
    public void removeCookies() {
        Selenide.clearBrowserCookies();
        Selenide.clearBrowserLocalStorage();
    }

    @AfterClass
    public void tearDown() {
        Selenide.closeWebDriver();
        Report.generate();
    }

    private void initBrowser() {
        Configuration.pageLoadStrategy = "eager";
        Configuration.screenshots = false;

        WebDriverManager.chromedriver().setup();
        ChromeOptions chromeOptions = new ChromeOptions();
        chromeOptions.addArguments("--window-size=1920,1080",
                "--headless",
                "--disable-gpu",
                "--ignore-certificate-errors",
                "--silent",
                "--hide-scrollbars",
                "--no-sandbox",
                "--disable-dev-shm-usage");
        WebDriver driver = new ChromeDriver(chromeOptions);
        WebDriverRunner.setWebDriver(driver);
    }
}
