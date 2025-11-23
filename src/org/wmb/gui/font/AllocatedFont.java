package org.wmb.gui.font;

import org.bfg.generate.BitmapFont;
import org.bfg.generate.BitmapFontGenerator;
import org.bfg.generate.GlyphRange;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Objects;

public class AllocatedFont {

    private final AllocatedGlyph[] glyphs;
    private final int leading;

    public AllocatedFont(Font font, int charCount) {
        Objects.requireNonNull(font, "Font is null");

        if (charCount < 1)
            throw new IllegalArgumentException("Char count is less than 1");

        final GlyphRange range = new GlyphRange((char) (charCount - 1)); // Range inclusive
        final BitmapFont bitmapFont = BitmapFontGenerator.generate(font, range, true);
        this.leading = bitmapFont.getLeading();
        this.glyphs = new AllocatedGlyph[charCount];
        for (char c = 0; c < charCount; c++) {
            final BufferedImage glyphImage = bitmapFont.extrudeGlyph(c);
            this.glyphs[c] = new AllocatedGlyph(glyphImage);
        }
    }

    public AllocatedGlyph getGlyph(char c) {
        if (c >= this.glyphs.length)
            return null;

        return this.glyphs[c];
    }

    public Dimension getStringSize(String string) {
        if (string == null)
            string = "null";

        int width = 0;
        int height = 0;
        for (int index = 0; index < string.length(); index++) {
            final AllocatedGlyph glyph = this.glyphs[string.charAt(index)];
            if (glyph == null)
                continue;

            width += glyph.width + this.leading;
            height = Math.max(height, glyph.height);
        }

        return new Dimension(width, height);
    }

    public int getLeading() {
        return this.leading;
    }

    public void delete() {
        for (AllocatedGlyph glyph : this.glyphs)
            glyph.delete();
    }
}
