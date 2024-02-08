package core;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import rendering.Texture;
import shaders.ScreenSpace2dShader;

import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL30.*;

public class Scene
{
    private static final float[] vertices = {
            // positions            // texture coordinates
            0.5f,  0.5f, 0.0f,      1.0f, 1.0f,     // top right
            0.5f, -0.5f, 0.0f,      1.0f, 0.0f,     // bottom right
            -0.5f, -0.5f, 0.0f,     0.0f, 0.0f,     // bottom left
            -0.5f,  0.5f, 0.0f,     0.0f, 1.0f      // top left
    };

    private static final int[] indices = {
            0, 1, 3,   // first triangle
            1, 2, 3    // second triangle
    };

    private static int vboID, vaoID, eboID;
    private static ScreenSpace2dShader screenSpace2d_shader;
    private static Texture bricks;
    private static Matrix4f transform;
    private static Matrix4f projection;

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

        glVertexAttribPointer(0, 3, GL_FLOAT, false, 5 * Float.BYTES, 0);
        glEnableVertexAttribArray(0);

        glVertexAttribPointer(1, 2, GL_FLOAT, false, 5 * Float.BYTES, 3 * Float.BYTES);
        glEnableVertexAttribArray(1);

        glPolygonMode(GL_FRONT_FACE, GL_FILL);

        screenSpace2d_shader = new ScreenSpace2dShader("./res/shaders/ScreenSpace2D.glsl");
        bricks = new Texture("./res/textures/bricks_01.jpg", true);

        transform = new Matrix4f().identity();
        transform.translate(new Vector3f(0.0f, 0.0f, 0.0f));
        transform.rotateXYZ(new Vector3f(0.0f, 0.0f, 0.0f));
        transform.scale(new Vector3f(1, 1, 1));

        float aspectRatio = ((float)GlobalSettings.WINDOW_WIDTH / (float)GlobalSettings.WINDOW_HEIGHT);
        projection = new Matrix4f().ortho(-aspectRatio, aspectRatio, -1.0f, 1.0f, -1.0f, 1.0f);
    }

    public static void pollInput()
    {}

    public static void update()
    {}

    public static void fixedPhysicsUpdate()
    {
        transform.rotateXYZ(0.0f, 0.0f, (float)Math.toRadians(45.0) * GlobalSettings.fixedPhysicsTimeStep);
    }

    public static void render()
    {
        screenSpace2d_shader.bind();
        bricks.bind(0);
        screenSpace2d_shader.updateUniforms(new Vector4f(1.0f, 1.0f, 1.0f, 1.0f), transform, projection);
        glBindVertexArray(vaoID);
        glDrawElements(GL_TRIANGLES, indices.length, GL_UNSIGNED_INT, 0);
        glBindVertexArray(0);
        screenSpace2d_shader.unbind();
    }
}