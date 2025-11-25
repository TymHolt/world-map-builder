package org.wmb.gui.font;

import org.bfg.generate.BitmapFont;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Objects;

public class AllocatedFont {

    private final AllocatedGlyph[] glyphs;
    private final int leading;

    AllocatedFont(BitmapFont bitmapFont) {
        Objects.requireNonNull(bitmapFont, "Font is null");

        final char charCount = (char) bitmapFont.getRange().getCount();
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
