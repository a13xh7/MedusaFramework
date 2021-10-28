package medusa;

import java.awt.*;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class Config {

    // Diff settings
    public static int scrollTimeout = 1000;
    public static int diffColor = Color.MAGENTA.getRGB();
    public static float diffOpacity = 0.5f;//0.4f;
    public static int colorTolerance = 20;

    public static String breakpointDelimiter = "__";

    //test run settings
    public static String browser = "chrome";
    public static String clean = "0";
    public static String headless = "1";
    public static String env = "dev";

    private static Properties properties;
    public static int allowableDiffSize = 10;

    public static void initConfig()
    {
        browser = System.getProperty("browser") == null ? "chrome" : System.getProperty("browser");
        clean = System.getProperty("clean") == null ? "0" : System.getProperty("clean");
        headless = System.getProperty("headless") == null ? "1" : System.getProperty("headless");
        env = System.getProperty("env") == null ? "dev" : System.getProperty("env");

        properties = new Properties();
        try {
            FileInputStream fis = new FileInputStream("config/visual.properties");
            properties.load(fis);
        } catch (IOException e) {
            System.err.println("Config file not found");
        }
        allowableDiffSize = Integer.parseInt(properties.getProperty("allowableDiffSize"));
    }

    // PATS TO SCREENSHOTS

    public static String pathToExpected() {
        return properties.getProperty("screenshots.expected");
    }

    public static String pathToActual() {
        return properties.getProperty("screenshots.actual");
    }

    public static String pathToDiff() {
        return properties.getProperty("screenshots.diff");
    }

    public static String pathToGif() {
        return properties.getProperty("screenshots.gif");
    }

    // REPORT TEMPLATE

    public static String pathToReportTemplate() {
        return properties.getProperty("report.template");
    }

    public static String pathToReportOutput() {
        return properties.getProperty("report.output");
    }

    public static String pathToErrorsLog() {
        return properties.getProperty("report.errors_log");
    }

    // BREAKPOINTS ARRAY
    public static String[] breakpoints() {
        return properties.getProperty("breakpoints").split(",");
    }

}
