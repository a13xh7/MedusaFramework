package app;

import app.pages.*;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.WebDriverRunner;
import helpers.Trim;
import helpers.Wait;
import static com.codeborne.selenide.Selenide.*;

public class App {

    public IndexPage indexPage;

    public App() {
        AppConfig.initConfig();
        indexPage = PageBuilder.buildIndexPage();
    }

    public void open() {
        Selenide.open(AppConfig.baseUrl);
    }

    public void openPage(String pageUrl) {
        String url = Trim.rtrim(AppConfig.baseUrl, "/") + "/" + Trim.ltrim(pageUrl, "/");
        WebDriverRunner.getWebDriver().get(url);
    }

    public void openUrl(String url) {
        Selenide.open(url);
    }

    // PREPARE
    public void preparePageForScreenshot() {
        scrollBottomTop(1);
    }

    public void preparePageForScreenshot(int delayBetweenScroll) {
        scrollBottomTop(delayBetweenScroll);
    }

    public void scrollBottomTop(int delayBetweenScroll) {
        executeJavaScript("window.scrollTo(0,document.body.scrollHeight);");
        Wait.wait(delayBetweenScroll);
        executeJavaScript("window.scrollTo(0,0);");
    }

    // Remove common elements - example
    // add it to preparePageForScreenshot()
    public void removeHeader() {
        try {
            executeJavaScript("document.querySelector('div.layout__top').style.display = 'none'");
        }catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
