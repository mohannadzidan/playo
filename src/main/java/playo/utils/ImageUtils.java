package playo.utils;

import javafx.geometry.Rectangle2D;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

public final class ImageUtils {
    private ImageUtils() {
    } // prevent instantiation

    public static Rectangle2D aspectRatioViewport(Image image, double w, double h) {
        double widthToHeightRatio = w / h;
        double width = image.getWidth();
        double height = image.getHeight();
        double centerX = width / 2, centerY = height / 2;
        double minDim = Math.min(width, height);
        if (w >= h) {
            width = minDim;
            height = width * 1 / widthToHeightRatio;
        } else {
            height = minDim;
            width = height * widthToHeightRatio;
        }
        double minX = centerX - width / 2, minY = centerY - height / 2;
        return new Rectangle2D(minX, minY, width, height);
    }

    public static Color avgColor(Image image) {
        var reader = image.getPixelReader();
        double widthStep = image.getWidth() / 5.0;
        double heightStep = image.getHeight() / 5.0;
        double totalRed = 0, totalBlue = 0, totalGreen = 0;
        int totalPoints = 0;
        for (double x = widthStep / 2; x < image.getWidth(); x += widthStep) {
            for (double y = heightStep / 2; y < image.getHeight(); y += heightStep) {
                Color c = reader.getColor((int) x, (int) y);
                totalRed += c.getRed();
                totalBlue += c.getBlue();
                totalGreen += c.getGreen();
                totalPoints++;
            }
        }
        var avg = new Color(totalRed / totalPoints, totalGreen / totalPoints, totalBlue / totalPoints, 1);
        System.out.println(avg);
        return new Color(totalRed / totalPoints, totalGreen / totalPoints, totalBlue / totalPoints, 1);
    }

}
