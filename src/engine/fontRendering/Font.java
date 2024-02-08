package engine.fontRendering;

import engine.rendering.Texture;

public class Font
{
    public String name;
    public FontGlyph[] glyphs;
    public Texture bitmap;
    public int size;

    public FontGlyph getGlyphOfValue(int asciiValue)
    {
        FontGlyph glyph = null;

        for (FontGlyph fg: glyphs)
        {
            if (fg.characterID == asciiValue)
                glyph = fg;
        }

        if (glyph == null)
            System.err.println("No font glyph of value " + asciiValue + " found in font " + name + "'s glyph map!");
        else
            return glyph;

        return null;
    }

    public Font(String name, FontGlyph[] glyphs, Texture bitmap, int size)
    {
        this.name = name;
        this.glyphs = glyphs;
        this.bitmap = bitmap;
        this.size = size;
    }
}