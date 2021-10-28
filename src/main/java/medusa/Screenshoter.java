package medusa;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import javax.imageio.ImageIO;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.*;
import org.openqa.selenium.Point;
import org.openqa.selenium.remote.Augmenter;

public class Screenshoter {

    private static BufferedImage cachedScreenshot;

    public static Screenshot shootPage(WebDriver driver) {
        cachedScreenshot = getCombinedScreenshot(driver);
        return new Screenshot(cachedScreenshot);
    }

    public static Screenshot shootElement(WebDriver driver, WebElement element) {
        cachedScreenshot = getElementScreenshot(driver, element);
        return new Screenshot(cachedScreenshot);
    }

    public static Screenshot generateDifferenceImage(String expectedImagePath, String actualImagePath) {
        return Differ.generateDifferenceImage(expectedImagePath, actualImagePath);
    }

    public static void generateGifImage(String pathToExpectedImg, String pathToActualImg, String pathToDiffImg, String pathToResultGif) {
        GifGenerator.createGif(pathToExpectedImg, pathToActualImg, pathToDiffImg, pathToResultGif);
    }

    private static BufferedImage getElementScreenshot(WebDriver driver, WebElement element) {

        // Get entire page screenshot

        BufferedImage fullImg = getCombinedScreenshot(driver);

        ((JavascriptExecutor) driver).executeScript("document.querySelector('html').style.scrollBehavior = 'auto';");
        ((JavascriptExecutor) driver).executeScript("window.scrollTo(0,0);");

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Get the location of element on the page

        Point point = element.getLocation();

        // Get width and height of the element

        int elementWidth = element.getSize().getWidth();
        int elementHeight = element.getSize().getHeight();

        // Crop the entire page screenshot to get only element screenshot

        BufferedImage elementScreenshot = fullImg.getSubimage(point.getX(), point.getY(), elementWidth, elementHeight);

        return elementScreenshot;
    }

    private static String getFullHeight(WebDriver driver) {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        return js.executeScript("return document.body.scrollHeight").toString();
    }

    private static int getFullWidth(WebDriver driver) {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        return ((Long) js.executeScript("return window.innerWidth", new Object[0])).intValue();
    }

    private static int getWindowHeight(WebDriver driver) {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        return ((Long) js.executeScript("return window.innerHeight", new Object[0])).intValue();
    }

    private static void waitForScrolling() {
        try {
            Thread.sleep(Config.scrollTimeout);
        } catch (InterruptedException ignored) {
        }
    }

    private static BufferedImage getScreenshotNative(WebDriver wd) {
        ByteArrayInputStream imageArrayStream = null;
        TakesScreenshot takesScreenshot = (TakesScreenshot) new Augmenter().augment(wd);

        try {
            imageArrayStream = new ByteArrayInputStream(takesScreenshot.getScreenshotAs(OutputType.BYTES));
            return ImageIO.read(imageArrayStream);
        } catch (IOException e) {
            throw new RuntimeException("Can not parse screenshot data", e);
        } finally {
            try {
                if (imageArrayStream != null) {
                    imageArrayStream.close();
                }
            } catch (IOException ignored) {
            }
        }
    }

    private static BufferedImage getCombinedScreenshot(WebDriver wd) {
        JavascriptExecutor js = (JavascriptExecutor) wd;

        int allH = Integer.parseInt(getFullHeight(wd));
        int allW = getFullWidth(wd);
        int winH = getWindowHeight(wd);
        int scrollTimes = allH / winH;
        int tail = allH - winH * scrollTimes;

        BufferedImage finalImage = new BufferedImage(allW, allH, BufferedImage.TYPE_4BYTE_ABGR);
        Graphics2D graphics = finalImage.createGraphics();

        for (int n = 0; n < scrollTimes; n++) {
            js.executeScript("scrollTo(0, arguments[0])", winH * n);
            waitForScrolling();
            BufferedImage part = getScreenshotNative(wd);
            graphics.drawImage(part, 0, n * winH, null);
        }

        if (tail > 0) {
            js.executeScript("scrollTo(0, document.body.scrollHeight)");
            waitForScrolling();
            BufferedImage last = getScreenshotNative(wd);
            BufferedImage tailImage = last.getSubimage(0, last.getHeight() - tail, last.getWidth(), tail);
            graphics.drawImage(tailImage, 0, scrollTimes * winH, null);
        }

        graphics.dispose();

        return finalImage;
    }

    // Manage Files

    public static void removeAll() {
        removeActualScreenshots();
        removeDiffScreenshots();
        removeExpectedScreenshots();
        removeGifScreenshots();
    }

    public static void createScreenshotsFolders() {
        try {
            Files.createDirectories(Paths.get(Config.pathToExpected()));
            Files.createDirectories(Paths.get(Config.pathToActual()));
            Files.createDirectories(Paths.get(Config.pathToDiff()));
            Files.createDirectories(Paths.get(Config.pathToGif()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String getRealName(String testName, String breakpoint, String extension) {
        return testName + Config.breakpointDelimiter + Config.env + "_" + Config.browser + "_" + breakpoint + "." + extension;
    }

    public static void removeExpectedScreenshots()
    {
        File directory = new File(Config.pathToExpected());
        try {
            FileUtils.cleanDirectory(directory);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void removeActualScreenshots()
    {
        File directory = new File(Config.pathToActual());
        try {
            FileUtils.cleanDirectory(directory);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void removeDiffScreenshots()
    {
        File directory = new File(Config.pathToDiff());
        try {
            FileUtils.cleanDirectory(directory);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void removeGifScreenshots()
    {
        File directory = new File(Config.pathToGif());
        try {
            FileUtils.cleanDirectory(directory);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}