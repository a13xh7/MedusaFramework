package medusa;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;

public class Differ {

    public static Screenshot generateDifferenceImage(String expectedImagePath, String actualImagePath) {

        BufferedImage result = new BufferedImage(100, 100, BufferedImage.TYPE_INT_ARGB);
        int diffSize = 0;

        try {
            // get expected and actual images

            BufferedImage expectedImage = ImageIO.read(new File(expectedImagePath));
            BufferedImage actualImage = ImageIO.read(new File(actualImagePath));

            int resultImgWidth = expectedImage.getWidth();
            int resultImgHeight = expectedImage.getHeight();

            // convert images to pixel arrays

            int[] expectedImagePixels = expectedImage.getRGB(0, 0, resultImgWidth, resultImgHeight, null, 0, resultImgWidth);
            int[] actualImagePixels = actualImage.getRGB(0, 0, actualImage.getWidth(), actualImage.getHeight(), null, 0, actualImage.getWidth());

            // compare expected image to actual image.
            // paint different pixels

            try {

                for (int i = 0; i < expectedImagePixels.length; i++) {
                    if ( !arePixelsEqual(new Color(expectedImagePixels[i]), new Color(actualImagePixels[i]), Config.colorTolerance)  ) {
                        expectedImagePixels[i] = Config.diffColor;
                        diffSize++;
                    }
                }

            } catch (Exception e) {}

            // Expected image is base

            result = new BufferedImage(resultImgWidth, resultImgHeight, BufferedImage.TYPE_INT_ARGB);
            result.setRGB(0, 0, resultImgWidth, resultImgHeight, expectedImagePixels, 0, resultImgWidth);

            // Actual image is overlay

            BufferedImage diffImageOverlay = new BufferedImage(resultImgWidth, resultImgHeight, BufferedImage.TYPE_INT_ARGB);
            diffImageOverlay.setRGB(0, 0, resultImgWidth, resultImgHeight, expectedImagePixels, 0, resultImgWidth);

            // Paint overlay on top of base image

            Graphics g = result.getGraphics();
            g.drawImage(expectedImage, 0, 0, null);
            g.drawImage(setImageOpacity(diffImageOverlay, Config.diffOpacity), 0, 0, null);
            g.dispose();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return new Screenshot(result, diffSize);
    }

    private static BufferedImage setImageOpacity(BufferedImage source, float opacity) {
        BufferedImage target = new BufferedImage(source.getWidth(), source.getHeight(), java.awt.Transparency.TRANSLUCENT);
        // Get the images graphics
        Graphics2D g = target.createGraphics();
        // Set the Graphics composite to Alpha
        g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, opacity));
        // Draw the image into the prepared receiver image
        g.drawImage(source, null, 0, 0);
        // let go of all system resources in this Graphics
        g.dispose();
        // Return the image
        return target;
    }

    private static boolean arePixelsEqual(Color target, Color pixel, int tolerance) {
        int rT = target.getRed();
        int gT = target.getGreen();
        int bT = target.getBlue();

        int rP = pixel.getRed();
        int gP = pixel.getGreen();
        int bP = pixel.getBlue();

        return(
            (rP-tolerance<=rT) && (rT<=rP+tolerance) &&
            (gP-tolerance<=gT) && (gT<=gP+tolerance) &&
            (bP-tolerance<=bT) && (bT<=bP+tolerance)
        );
    }
}
