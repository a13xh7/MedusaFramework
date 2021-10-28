package medusa;

import com.codeborne.selenide.WebDriverRunner;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebElement;

import java.io.File;
import java.io.PrintWriter;

public class Medusa {

    private static boolean initialized = false;

    public static void init() {
        if (!initialized) {
            initialized = true;

            Config.initConfig();

            clearErrorsLog();

            Screenshoter.createScreenshotsFolders();

            if(Config.clean.equals("1")) {
                Screenshoter.removeExpectedScreenshots();
            }

            Screenshoter.removeActualScreenshots();
            Screenshoter.removeDiffScreenshots();
            Screenshoter.removeGifScreenshots();
        }
    }

    public static void testPage(String testName, String ...breakpoints) {
        test(testName, false, null, breakpoints);
    }

    public static void testElement(String testName, WebElement element, String ...breakpoints) {
        test(testName, true, element, breakpoints);
    }

    private static void test(String testName, boolean isElement, WebElement element, String ...breakpoints) {
        String[] breakpointsForLoop = Config.breakpoints();

        if(breakpoints.length != 0) {
            breakpointsForLoop = breakpoints;
        }

        for (String breakpoint : breakpointsForLoop)
        {
            WebDriverRunner.getWebDriver().manage().window().setSize(new Dimension(Integer.parseInt(breakpoint), 1080));

            String fileName = Screenshoter.getRealName(testName, breakpoint, "png");
            String gifFileName = Screenshoter.getRealName(testName, breakpoint, "gif");

            String pathToExpected = Config.pathToExpected() + fileName;
            String pathToActual = Config.pathToActual() + fileName;
            String pathToDiff = Config.pathToDiff() + fileName;
            String pathToGif = Config.pathToGif() + gifFileName;

            // Save actual screenshot

            Screenshot img;

            if(isElement) {
                img = Screenshoter.shootElement(WebDriverRunner.getWebDriver(), element);
            } else {
                img = Screenshoter.shootPage(WebDriverRunner.getWebDriver());
            }

            img.save(pathToActual);

            // If there isn't expected screenshot, save actual screenshot as expected

            tryToSaveActualAsExpected(img, pathToExpected);

            // Compare actual and expected screenshots. If there is difference create difference and gif images

            Screenshot diffImg = Screenshoter.generateDifferenceImage(pathToExpected, pathToActual);

            if(diffImg.getDiffSize() > Config.allowableDiffSize)
            {
                diffImg.save(pathToDiff);
                Screenshoter.generateGifImage(pathToExpected, pathToActual, pathToDiff, pathToGif);
            }
        }
    }

    private static void tryToSaveActualAsExpected(Screenshot img, String pathToExpected) {
        File expectedScreenshot = new File(pathToExpected);

        if(!expectedScreenshot.exists()) {
            img.save(pathToExpected);
        }
    }

    private static void clearErrorsLog() {
        try {
            PrintWriter writer = new PrintWriter(Config.pathToErrorsLog(), "UTF-8");
            writer.println("");
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
