package org.wmb.loading;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public final class ResourceLoader {

    public static InputStream loadStream(String path) throws IOException {
        final InputStream inputStream = ResourceLoader.class.getResourceAsStream(path);
        if (inputStream == null)
            throw new IOException("No stream for resource " + path);

        return inputStream;
    }

    public static String loadText(String path) throws IOException {
            return new String(loadStream(path).readAllBytes(), StandardCharsets.UTF_8);
    }

    public static BufferedImage loadImage(String path) throws IOException {
        return ImageIO.read(loadStream(path));
    }
}
