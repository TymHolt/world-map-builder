package org.wmb.gui.font;

import org.bfg.generate.BitmapFont;
import org.wmb.rendering.OpenGLStateException;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Objects;

public class AllocatedFont {

    private final AllocatedGlyph[] glyphs;
    private final int leading;

    AllocatedFont(BitmapFont bitmapFont) throws OpenGLStateException {
        Objects.requireNonNull(bitmapFont, "Font is null");

        final char charCount = (char) bitmapFont.getRange().getCount();
        this.leading = bitmapFont.getLeading();
        this.glyphs = new AllocatedGlyph[charCount];
        for (char c = 0; c < charCount; c++) {
            final BufferedImage glyphImage = bitmapFont.extrudeGlyph(c);

            try {
                this.glyphs[c] = new AllocatedGlyph(glyphImage);
            } catch (OpenGLStateException exception) {
                // Delete all already allocated glyphs
                for (AllocatedGlyph glyph : this.glyphs)
                    if (glyph != null)
                        glyph.delete();

                throw exception;
            }
        }
    }

    public AllocatedGlyph getGlyph(char c) {
        if (c >= this.glyphs.length)
            return null;

        return this.glyphs[c];
    }

    public int getLeading() {
        return this.leading;
    }

    public void delete() {
        for (AllocatedGlyph glyph : this.glyphs)
            glyph.delete();
    }
}
