package helpers;

import java.awt.*;
import java.awt.image.BufferedImage;

public class ImageEditor {
    public static Image rescaleToHeight(BufferedImage image, int targetHeight) {
        double coefficient = (double) image.getHeight() / (double) targetHeight;
        int width = (int) ((double) image.getWidth() / coefficient);
        int height = (int) ((double) image.getHeight() / coefficient);
        return image.getScaledInstance(width, height, BufferedImage.SCALE_AREA_AVERAGING);
    }
}
