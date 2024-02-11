package engine.rendering;

import org.joml.Vector2f;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

public class SpriteSheetDataLoader
{
    public static Vector2f[] loadSheetDataFromPath(String filepath)
    {
        try
        {
            File file = new File(filepath);
            BufferedReader reader = new BufferedReader(new FileReader(file));
            List<Vector2f> offsets = new ArrayList<>();

            String currentLine;
            while ((currentLine = reader.readLine()) != null)
            {
                String[] axis = currentLine.split(",");
                offsets.add(new Vector2f(Float.parseFloat(axis[0]), Float.parseFloat(axis[1])));
            }

            Vector2f[] out = new Vector2f[offsets.size()];
            for (int i = 0; i < out.length; i++)
                out[i] = offsets.get(i);

            return out;
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return null;
    }
}