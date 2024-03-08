package engine.rendering;

import engine.core.Entity;
import engine.core.EntityType;
import engine.core.GlobalSettings;
import engine.fontRendering.*;
import engine.shaders.ScreenSpace2dShader;
import org.joml.*;

import java.util.ArrayList;

import static org.lwjgl.opengl.GL11.GL_FLOAT;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;

public class TextMesh extends Entity
{
    public final Font font;
    public final ScreenSpace2dShader screenSpace2dShader;
    public int vboID, vaoID, eboID;
    public float[] vertexData;
    public int[] indexData;
    public String lastDrawCall;
    public float fontSize_PIXELS;
    public Vector2i locationAnchor;
    public boolean isLocationAnchored;
    public Vector2f boundSize;
    public Vector2i textAlignment;
    public String text;
    public Vector4f colorRGBA;
    public Vector3f lastCalculatedScreenPosition;

    private final float BASE_SCALE = 0.01408450704225f;

    public TextMesh(String name, Font font, boolean isLocationAnchored)
    {
        super(name, EntityType.TextMesh);
        this.font = font;
        screenSpace2dShader = new ScreenSpace2dShader("res/shaders/ScreenSpace2D.glsl");
        lastDrawCall = "";
        position = new Vector3f(0.0f);
        rotation = new Vector3f(0.0f);
        fontSize_PIXELS = 12;
        locationAnchor = new Vector2i(0);
        this.isLocationAnchored = isLocationAnchored;
        boundSize = new Vector2f(0.0f);
        textAlignment = new Vector2i(1, 1);
        text = "Hello world!";
        colorRGBA = new Vector4f(1.0f);
        lastCalculatedScreenPosition = new Vector3f(0.0f);
    }

    public void render()
    {
        drawString();
    }

    public void drawString()
    {
        if (!text.equals(lastDrawCall) || lastDrawCall.isEmpty())
        {
            lastDrawCall = text;
            char[] characters = text.toCharArray();
            FontGlyph[] glyphs = new FontGlyph[characters.length];

            for (int i = 0; i < glyphs.length; i++)
                glyphs[i] = font.getGlyphOfValue(characters[i]);

            buildMeshFromGlyphsArray(glyphs);
            generateOpenGLObjects();
            recalculateBounds();
        }

        Vector3f finalPosition;
        if (isLocationAnchored)
        {
            float halfWidth = (float) GlobalSettings.WINDOW_WIDTH / 2.0f;
            float halfHeight = (float)GlobalSettings.WINDOW_HEIGHT / 2.0f;
            float x = halfWidth * (float)locationAnchor.x;
            float y = halfHeight * (float)locationAnchor.y;
            finalPosition = new Vector3f(x + position.x, y + position.y, position.z);
        }
        else
        {
            finalPosition = position;
        }

        Vector3f textAlignmentPos = new Vector3f(-boundSize.x/2 + (textAlignment.x * boundSize.x/2), boundSize.y/2 + (boundSize.y/2 * textAlignment.y), 0.0f);
        finalPosition.add(textAlignmentPos);

        // TODO: fix bug causing text to move continuously when not location anchored
        //  if it's broken here, it's broken in ScreenSpaceSprite as well. fix it there too!
        Matrix4f transform = new Matrix4f().identity();
        transform.translate(finalPosition);
        transform.rotateXYZ(rotation);
        transform.scale(new Vector3f(BASE_SCALE).mul(fontSize_PIXELS));

        transform.getTranslation(lastCalculatedScreenPosition);
        lastCalculatedScreenPosition.add(new Vector3f((float) GlobalSettings.WINDOW_WIDTH / 2.0f, (float)GlobalSettings.WINDOW_HEIGHT / 2.0f, 0.0f));

        bindMaterial();
        updateUniforms(colorRGBA, transform);
        renderMesh();
    }

    private void buildMeshFromGlyphsArray(FontGlyph[] glyphs)
    {
        ArrayList<Float> vertices = new ArrayList<>();
        ArrayList<Integer> indices = new ArrayList<>();
        float cursor = 0;

        for (int i = 0; i < glyphs.length; i++)
        {
            FontGlyph glyph = glyphs[i];

            float posX = glyph.position.x;
            float posY = glyph.position.y;
            float width = glyph.dimensions.x;
            float height = glyph.dimensions.y;
            float offsetX = glyph.offset.x;
            float offsetY = glyph.offset.y;

            // Top left
            vertices.add(cursor + offsetX);
            vertices.add(-offsetY);
            vertices.add(0.0f);

            vertices.add((posX) / 512.0f);
            vertices.add((posY) / 512.0f);

            // Top right
            vertices.add(cursor + offsetX + width);
            vertices.add(-offsetY);
            vertices.add(0.0f);

            vertices.add((posX + width) / 512.0f);
            vertices.add((posY) / 512.0f);

            // Bottom right
            vertices.add(cursor + offsetX + width);
            vertices.add(-offsetY - height);
            vertices.add(0.0f);

            vertices.add((posX + width) / 512.0f);
            vertices.add((posY + height) / 512.0f);

            // Bottom left
            vertices.add(cursor + offsetX);
            vertices.add(-offsetY - height);
            vertices.add(0.0f);

            vertices.add((posX) / 512.0f);
            vertices.add((posY + height) / 512.0f);

            indices.add(i * 4);
            indices.add(i * 4 + 3);
            indices.add(i * 4 + 2);

            indices.add(i * 4 + 2);
            indices.add(i * 4 + 1);
            indices.add(i * 4);

            cursor += glyph.advance;
        }

        vertexData = new float[vertices.size()];
        indexData = new int[indices.size()];

        for (int i = 0; i < vertexData.length; i++)
            vertexData[i] = vertices.get(i);

        for (int i = 0; i < indexData.length; i++)
            indexData[i] = indices.get(i);
    }

    private void generateOpenGLObjects()
    {
        vboID = glGenBuffers();
        vaoID = glGenVertexArrays();
        eboID = glGenBuffers();

        glBindVertexArray(vaoID);

        glBindBuffer(GL_ARRAY_BUFFER, vboID);
        glBufferData(GL_ARRAY_BUFFER, vertexData, GL_STATIC_DRAW);

        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, eboID);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, indexData, GL_STATIC_DRAW);

        glVertexAttribPointer(0, 3, GL_FLOAT, false, 5 * Float.BYTES, 0);
        glEnableVertexAttribArray(0);

        glVertexAttribPointer(1, 2, GL_FLOAT, false, 5 * Float.BYTES, 3 * Float.BYTES);
        glEnableVertexAttribArray(1);
    }

    public void recalculateBounds()
    {
        float minX = Float.MIN_VALUE;
        float minY = Float.MIN_VALUE;

        float maxX = Float.MIN_VALUE;
        float maxY = Float.MIN_VALUE;

        for (int i = 0; i < vertexData.length / 5; i++)
        {
            float x = vertexData[i * 5];
            float y = vertexData[i * 5 + 1];

            if (x < minX)
                minX = x;

            if (y < minY)
                minY = y;

            if (x > maxX)
                maxX = x;

            if (y > maxY)
                maxY = y;
        }

        boundSize = new Vector2f((maxX - minX) * BASE_SCALE * fontSize_PIXELS, (maxY - minY) * BASE_SCALE * fontSize_PIXELS);
    }

    private void bindMaterial()
    {
        font.bitmap.bind(0);
        screenSpace2dShader.bind();
    }

    private void updateUniforms(Vector4f color, Matrix4f transform)
    {
        screenSpace2dShader.updateUniforms(color, transform);
    }

    private void renderMesh()
    {
        glBindVertexArray(vaoID);
        glDrawElements(GL_TRIANGLES, indexData.length, GL_UNSIGNED_INT, 0);
        glBindVertexArray(0);
    }
}