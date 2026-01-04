package org.wmb.loading;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public final class ResourceLoader {

    public static String loadFileText(String path) throws IOException {
        final FileInputStream fileInputStream = new FileInputStream(path);
        final String text = new String(fileInputStream.readAllBytes(), StandardCharsets.UTF_8);
        fileInputStream.close();
        return text;
    }

    public static BufferedImage loadFileImage(String path) throws IOException {
        return ImageIO.read(new FileInputStream(path));
    }

    public static InputStream loadResourceStream(String path) throws IOException {
        final InputStream inputStream = ResourceLoader.class.getResourceAsStream(path);
        if (inputStream == null)
            throw new IOException("No stream for resource " + path);

        return inputStream;
    }

    public static String loadResourceText(String path) throws IOException {
        final InputStream inputStream = loadResourceStream(path);
        final String text = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
        inputStream.close();
        return text;
    }

    public static BufferedImage loadResourceImage(String path) throws IOException {
        return ImageIO.read(loadResourceStream(path));
    }
}
