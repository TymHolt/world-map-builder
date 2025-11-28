package org.wmb.gui.font;

import org.bfg.generate.BitmapFont;
import org.bfg.generate.BitmapFontGenerator;
import org.bfg.generate.GlyphInfo;
import org.bfg.generate.GlyphRange;
import org.wmb.gui.data.DynamicSize;
import org.wmb.rendering.OpenGLStateException;

import java.awt.Font;
import java.util.Objects;

public final class FontDefinition {

    private static final char CHAR_RANGE = (char) 255;

    private final BitmapFont bitmapFont;
    public final int textHeight;

    public FontDefinition(Font font) {
        Objects.requireNonNull(font, "Font is null");

        final GlyphRange range = new GlyphRange(CHAR_RANGE);
        this.bitmapFont = BitmapFontGenerator.generate(font, range, true);

        int height = 0;
        for (char c = 0; c < CHAR_RANGE; c++)
            height = Math.max(height, bitmapFont.getGlyphInfo(c).height);

        this.textHeight = height;
    }

    public void getTextSize(String text, DynamicSize destination) {
        if (text == null)
            text = "null";

        int width = 0;
        int height = 0;
        final int textLength = text.length();
        for (int index = 0; index < textLength; index++) {
            final GlyphInfo glyph = this.bitmapFont.getGlyphInfo(text.charAt(index));
            if (glyph == null)
                continue;

            width += glyph.width + this.bitmapFont.getLeading();
            height = Math.max(height, glyph.height);
        }

        destination.width = width;
        destination.height = height;
    }

    public DynamicSize getTextSize(String text) {
        final DynamicSize textSize = new DynamicSize(0, 0);
        getTextSize(text, textSize);
        return textSize;
    }

    public AllocatedFont allocate() throws OpenGLStateException {
        return new AllocatedFont(this.bitmapFont);
    }
}
