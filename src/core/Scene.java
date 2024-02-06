package core;

import shaders.Shader;

import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL30.*;

public class Scene
{
    private static final float[] vertices = {
            0.5f,  0.5f, 0.0f,  // top right
            0.5f, -0.5f, 0.0f,  // bottom right
            -0.5f, -0.5f, 0.0f,  // bottom left
            -0.5f,  0.5f, 0.0f   // top left
    };

    private static final int[] indices = {
            0, 1, 3,   // first triangle
            1, 2, 3    // second triangle
    };

    private static int vboID, vaoID, eboID;
    private static Shader screenSpace2d_shader;

    public static void initialize()
    {
        vboID = glGenBuffers();
        vaoID = glGenVertexArrays();
        eboID = glGenBuffers();

        glBindVertexArray(vaoID);

        glBindBuffer(GL_ARRAY_BUFFER, vboID);
        glBufferData(GL_ARRAY_BUFFER, vertices, GL_STATIC_DRAW);

        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, eboID);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, indices, GL_STATIC_DRAW);

        glVertexAttribPointer(0, 3, GL_FLOAT, false, 3 * Float.BYTES, 0);
        glEnableVertexAttribArray(0);

        screenSpace2d_shader = new Shader("./res/shaders/ScreenSpace2D.glsl");

        glPolygonMode(GL_FRONT_AND_BACK, GL_LINE);
    }

    public static void pollInput()
    {}

    public static void update()
    {}

    public static void fixedPhysicsUpdate()
    {}

    public static void render()
    {
        screenSpace2d_shader.bind();
        glBindVertexArray(vaoID);
        glDrawElements(GL_TRIANGLES, indices.length, GL_UNSIGNED_INT, 0);
        glBindVertexArray(0);
        screenSpace2d_shader.unbind();
    }
}