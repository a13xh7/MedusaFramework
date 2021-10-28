package helpers;

import com.codeborne.selenide.WebDriverRunner;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class Driver {

    public static void refresh() {
        WebDriverRunner.getWebDriver().navigate().refresh();
    }

    public static void waitForUrlContains(String urlChunk) {
        WebDriverWait wait = new WebDriverWait(WebDriverRunner.getWebDriver(), 10);
        wait.until(ExpectedConditions.urlContains(urlChunk));
    }

    public static void waitForUrlDoesNotContain(String urlChunk) {
        int maxTime = 20;
        while(  WebDriverRunner.getWebDriver().getCurrentUrl().contains(urlChunk)  && maxTime > 0) {
            Wait.wait(1);
            maxTime--;
        }
    }

    public static void changeWindowSize(int width, int height) {
        WebDriverRunner.getWebDriver().manage().window().setSize(new Dimension(width, height));
    }

    public static void maximize() {
        WebDriverRunner.getWebDriver().manage().window().maximize();
    }

    public static void wait(int seconds) {
        try {
            Thread.sleep(seconds * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
