package engine.fontRendering;

import org.joml.Vector2f;

public class FontGlyph
{
    public int characterID;
    public Vector2f position;
    public Vector2f dimensions;
    public Vector2f offset;
    public int advance;

    public FontGlyph(int characterID, Vector2f position, Vector2f dimensions, Vector2f offset, int advance)
    {
        this.characterID = characterID;
        this.position = position;
        this.dimensions = dimensions;
        this.offset = offset;
        this.advance = advance;
    }
}