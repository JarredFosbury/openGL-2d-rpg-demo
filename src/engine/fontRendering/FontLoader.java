package engine.fontRendering;

import engine.rendering.Texture;
import org.joml.Vector2f;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;

public class FontLoader
{
    public static Font loadFont(String bitmapSrc, String fontFileSrc)
    {
        try
        {
            Texture bitmap = new Texture(bitmapSrc, false, false, false);
            File sourceFile = new File(fontFileSrc);
            BufferedReader reader = new BufferedReader(new FileReader(sourceFile));
            String currentLine;

            String fontName = "";
            int fontSize = 0;
            ArrayList<FontGlyph> glyphs = new ArrayList<>();

            while ((currentLine = reader.readLine()) != null)
            {
                String[] chunks = currentLine.split(" ");

                if (chunks[0].equals("info"))
                {
                    String[] data  = currentLine.split("\"");
                    fontName = data[1];

                    String[] fntSz = chunks[3].split("=");
                    fontSize = Integer.parseInt(fntSz[1]);
                }

                if (chunks[0].equals("char"))
                {
                    FontGlyph glyph;
                    int charID = 0;
                    Vector2f pos = new Vector2f(0.0f, 0.0f);
                    Vector2f dimensions = new Vector2f(0.0f, 0.0f);
                    Vector2f offset = new Vector2f(0.0f, 0.0f);
                    int advance = 0;

                    for (int i = 0; i < chunks.length; i++)
                    {
                        String[] value = chunks[i].split("=");

                        if (value[0].equals("id"))
                            charID = Integer.parseInt(value[1]);

                        if (value[0].equals("x"))
                            pos = new Vector2f(Float.parseFloat(value[1]), 0.0f);

                        if (value[0].equals("y"))
                            pos = new Vector2f(pos.x, Float.parseFloat(value[1]));

                        if (value[0].equals("width"))
                            dimensions = new Vector2f(Float.parseFloat(value[1]), 0.0f);

                        if (value[0].equals("height"))
                            dimensions = new Vector2f(dimensions.x, Float.parseFloat(value[1]));

                        if (value[0].equals("xoffset"))
                            offset = new Vector2f(Float.parseFloat(value[1]), 0.0f);

                        if (value[0].equals("yoffset"))
                            offset = new Vector2f(offset.x, Float.parseFloat(value[1]));

                        if (value[0].equals("xadvance"))
                            advance = Integer.parseInt(value[1]);
                    }

                    glyphs.add(new FontGlyph(charID, pos, dimensions, offset, advance));
                }
            }

            FontGlyph[] glyphsArray = new FontGlyph[glyphs.size()];
            for (int i = 0; i < glyphsArray.length; i++)
                glyphsArray[i] = glyphs.get(i);

            return new Font(fontName, glyphsArray, bitmap, fontSize);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return null;
        }
    }
}