package helpers;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class ImageEditor {
    public static Image rescale(BufferedImage image, int targetWidth, int targetHeight) {
        double coefficient = (double) image.getHeight() / (double) targetHeight;
        int width = (int) ((double) image.getWidth() / coefficient);
        int height = (int) ((double) image.getHeight() / coefficient);

        if (width > targetWidth) {
            coefficient = (double) image.getWidth() / (double) targetWidth;
            width = (int) ((double) image.getWidth() / coefficient);
            height = (int) ((double) image.getHeight() / coefficient);
        }

        return image.getScaledInstance(width, height, BufferedImage.SCALE_AREA_AVERAGING);
    }

    public static BufferedImage loadImage(String path) throws IOException {
        return ImageIO.read(new File(path));
    }
}
