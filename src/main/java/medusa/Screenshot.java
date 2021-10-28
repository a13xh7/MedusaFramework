package medusa;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Screenshot {

    private BufferedImage cachedScreenshot;

    private int diffSize;

    public Screenshot(BufferedImage screenshot) {
        cachedScreenshot = screenshot;
    }

    public Screenshot(BufferedImage screenshot, int diffSize) {
        cachedScreenshot = screenshot;
        this.diffSize = diffSize;
    }

    public BufferedImage getImage() {
        return cachedScreenshot;
    }

    public int getDiffSize() {
        return diffSize;
    }

    public void save() {
        File dir = new File(System.getProperty("user.dir") + "/screenshots/");
        if (!dir.exists()) {
            dir.mkdirs();
        }
        saveFile(System.getProperty("user.dir") + "/screenshots/" + generateFilename() + ".png");
    }

    public void save(String imagePath) {
        try {
            ImageIO.write(cachedScreenshot, "png", new File(imagePath));
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    private void saveFile(String path) {
        try {
            ImageIO.write(cachedScreenshot, "png", new File(path));
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    private String generateFilename() {
        String screenshotTimestamp = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss").format(new Date());
        return screenshotTimestamp;
    }
}
