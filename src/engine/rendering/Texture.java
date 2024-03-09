package engine.rendering;

import java.nio.ByteBuffer;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.*;
import static org.lwjgl.opengl.GL30.glGenerateMipmap;
import static org.lwjgl.stb.STBImage.*;

public class Texture
{
    private final int[] TEXTURE_SLOTS;

    private final int textureID;
    private final String filepath;

    public Texture(String filepath, boolean flipVertically, boolean wrap, boolean nearest)
    {
        this.filepath = filepath;
        textureID = glGenTextures();

        glBindTexture(GL_TEXTURE_2D, textureID);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, wrap ? GL_REPEAT : GL_CLAMP);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, wrap ? GL_REPEAT : GL_CLAMP);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, nearest ? GL_NEAREST : GL_LINEAR);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, nearest ? GL_NEAREST : GL_LINEAR);

        int[] width = new int[1], height = new int[1], nrChannels = new int[1];
        stbi_set_flip_vertically_on_load(flipVertically);
        ByteBuffer data = stbi_load(filepath, width, height, nrChannels, 4);

        if (data == null)
        {
            System.err.println("TEXTURE IO ERROR: Could not load texture at path: " + filepath);
            System.exit(1);
        }

        data.flip();

        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, width[0], height[0], 0, GL_RGBA, GL_UNSIGNED_BYTE, data);
        glGenerateMipmap(GL_TEXTURE_2D);

        // frees texture data from RAM, NOT V-RAM. Freeing allocated texture data from V-RAM is driver
        // dependant behaviour and not something that can be done by calling a method yourself.
        // The glDeleteTextures function only promises that the texture name will be freed, not the actual memory.
        stbi_image_free(data);

        // map GL_TEXTURE values to index values for easy use of multiple texture slots
        TEXTURE_SLOTS = new int[16];
        TEXTURE_SLOTS[0] = GL_TEXTURE0;
        TEXTURE_SLOTS[1] = GL_TEXTURE1;
        TEXTURE_SLOTS[2] = GL_TEXTURE2;
        TEXTURE_SLOTS[3] = GL_TEXTURE3;
        TEXTURE_SLOTS[4] = GL_TEXTURE4;
        TEXTURE_SLOTS[5] = GL_TEXTURE5;
        TEXTURE_SLOTS[6] = GL_TEXTURE6;
        TEXTURE_SLOTS[7] = GL_TEXTURE7;
        TEXTURE_SLOTS[8] = GL_TEXTURE8;
        TEXTURE_SLOTS[9] = GL_TEXTURE9;
        TEXTURE_SLOTS[10] = GL_TEXTURE10;
        TEXTURE_SLOTS[11] = GL_TEXTURE11;
        TEXTURE_SLOTS[12] = GL_TEXTURE12;
        TEXTURE_SLOTS[13] = GL_TEXTURE13;
        TEXTURE_SLOTS[14] = GL_TEXTURE14;
        TEXTURE_SLOTS[15] = GL_TEXTURE15;
    }

    public void bind(int slot)
    {
        glActiveTexture(TEXTURE_SLOTS[slot]);
        glBindTexture(GL_TEXTURE_2D, textureID);
    }

    public int getTextureID()
    {
        return textureID;
    }

    public String getFilepath()
    {
        return filepath;
    }

    public void delete()
    {
        glBindTexture(GL_TEXTURE_2D, 0);
        glDeleteTextures(textureID);
    }
}