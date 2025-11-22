package org.wmb.gui;

import org.bfg.generate.BitmapFontGenerator;
import org.bfg.generate.CharInfo;
import org.joml.Vector4f;
import org.wmb.rendering.AllocatedData;
import org.wmb.rendering.AllocatedTexture;
import org.wmb.rendering.ITexture;
import org.wmb.rendering.TextureFilter;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.HashMap;

public class AllocatedFont implements ITexture, AllocatedData {

    private record CharBounds(int x, int y, int width, int height,
                              float u, float v, float uWidth, float vHeight) {

    }

    private final CharBounds[] charBounds;
    private final AllocatedTexture texture;
    private final int leading;

    public AllocatedFont(Font font, char charCount) {
        final BitmapFontGenerator bitmapFontGenerator = new BitmapFontGenerator(font, charCount,
            true);
        final HashMap<Character, CharInfo> absoluteBounds = bitmapFontGenerator.generateAll();
        final BufferedImage bitmap = bitmapFontGenerator.getImage();
        this.leading = bitmapFontGenerator.getLeading();
        bitmapFontGenerator.dispose();

        final int width = bitmap.getWidth();
        final int height = bitmap.getHeight();
        this.charBounds = new CharBounds[charCount];

        for (char index = 0; index < charCount; index++) {
            final CharInfo charInfo = absoluteBounds.get(index);
            final float uWidth = (float) charInfo.width / (float) width;
            final float vHeight = (float) charInfo.height / (float) height;
            final float u = (float) charInfo.x / (float) width;
            final float v = (float) charInfo.y / (float) height;

            this.charBounds[index] = new CharBounds(
                charInfo.x, charInfo.y, charInfo.width, charInfo.height, u, v, uWidth, vHeight);
        }

        this.texture = new AllocatedTexture(bitmap, TextureFilter.LINEAR);
    }

    public Vector4f getCharTexturePosition(char c) {
        if (c >= this.charBounds.length)
            return null;

        final CharBounds charBounds = this.charBounds[c];
        return new Vector4f(
            charBounds.u, charBounds.v, charBounds.uWidth, charBounds.vHeight
        );
    }

    public Dimension getCharSize(char c) {
        if (c >= this.charBounds.length)
            return null;

        final CharBounds bounds = this.charBounds[c];
        return new Dimension(bounds.width, bounds.height);
    }

    public Dimension getStringSize(String string) {
        if (string == null)
            string = "null";

        int width = 0;
        int height = 0;
        for (int index = 0; index < string.length(); index++) {
            final Dimension bounds = getCharSize(string.charAt(index));
            width += bounds.width + this.leading;
            height = Math.max(height, bounds.height);
        }

        return new Dimension(width, height);
    }

    public int getLeading() {
        return this.leading;
    }

    @Override
    public void delete() {
        this.texture.delete();
    }

    @Override
    public int getId() {
        return this.texture.getId();
    }
}
