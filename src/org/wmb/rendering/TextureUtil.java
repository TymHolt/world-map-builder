package org.wmb.rendering;

import java.awt.*;
import java.awt.image.BufferedImage;

public final class TextureUtil {

    public static BufferedImage getDebugBufferedImage(Color a, Color b) {
        BufferedImage image = new BufferedImage(2, 2, BufferedImage.TYPE_INT_ARGB);
        image.setRGB(0, 0, a.getRGB());
        image.setRGB(1, 1, a.getRGB());
        image.setRGB(0, 1, b.getRGB());
        image.setRGB(1, 0, b.getRGB());
        return image;
    }

    private static final Color DEFAULT_A = Color.BLACK;
    private static final Color DEFAULT_B = Color.PINK;

    public static BufferedImage getDebugBufferedImage() {
        return getDebugBufferedImage(DEFAULT_A, DEFAULT_B);
    }
}
