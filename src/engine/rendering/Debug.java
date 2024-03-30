package engine.rendering;

import engine.core.GlobalSettings;
import engine.core.Scene;
import engine.physics.Ray;
import engine.shaders.Debug2dShader;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;

import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.opengl.GL11.GL_FLOAT;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;

public class Debug
{
    private static int vboID, vaoID, eboID;
    private static float[] vertexData;
    private static int[] indexData;
    private static Debug2dShader shader;

    private static List<Float> drawQueue = new ArrayList<>(); // fist value is type
    private final static float DEBUG_RAY = 101.0f;
    private final static float DEBUG_CIRCLE = 102.0f;

    public static void drawRay(Ray ray, Vector4f color)
    {
        if (!GlobalSettings.renderDebug)
            return;

        drawQueue.add(DEBUG_RAY);
        drawQueue.add(ray.position.x);
        drawQueue.add(ray.position.y);
        drawQueue.add(ray.position.z);
        drawQueue.add(ray.direction.x);
        drawQueue.add(ray.direction.y);
        drawQueue.add(ray.direction.z);
        drawQueue.add(color.x);
        drawQueue.add(color.y);
        drawQueue.add(color.z);
        drawQueue.add(color.w);
    }

    public static void drawRay(Ray ray)
    {
        if (!GlobalSettings.renderDebug)
            return;

        drawQueue.add(DEBUG_RAY);
        drawQueue.add(ray.position.x);
        drawQueue.add(ray.position.y);
        drawQueue.add(ray.position.z);
        drawQueue.add(ray.direction.x);
        drawQueue.add(ray.direction.y);
        drawQueue.add(ray.direction.z);
        drawQueue.add(Color.DEBUG_DEFAULT_COLOR.x);
        drawQueue.add(Color.DEBUG_DEFAULT_COLOR.y);
        drawQueue.add(Color.DEBUG_DEFAULT_COLOR.z);
        drawQueue.add(Color.DEBUG_DEFAULT_COLOR.w);
    }

    public static void drawCircle(Vector3f position, float radius, Vector4f color)
    {
        if (!GlobalSettings.renderDebug)
            return;

        drawQueue.add(DEBUG_CIRCLE);
        drawQueue.add(position.x);
        drawQueue.add(position.y);
        drawQueue.add(position.z);
        drawQueue.add(radius);
        drawQueue.add(color.x);
        drawQueue.add(color.y);
        drawQueue.add(color.z);
        drawQueue.add(color.w);
    }

    public static void drawCircle(Vector3f position, float radius)
    {
        if (!GlobalSettings.renderDebug)
            return;

        drawQueue.add(DEBUG_CIRCLE);
        drawQueue.add(position.x);
        drawQueue.add(position.y);
        drawQueue.add(position.z);
        drawQueue.add(radius);
        drawQueue.add(Color.DEBUG_DEFAULT_COLOR.x);
        drawQueue.add(Color.DEBUG_DEFAULT_COLOR.y);
        drawQueue.add(Color.DEBUG_DEFAULT_COLOR.z);
        drawQueue.add(Color.DEBUG_DEFAULT_COLOR.w);
    }

    private static void buildMeshFromDrawQueue()
    {
        List<Float> vertices = new ArrayList<>();
        List<Integer> indices = new ArrayList<>();

        while (drawQueue.size() > 0)
        {
            if (drawQueue.get(0) == DEBUG_RAY)
                buildRayMesh(vertices, indices);
            else if (drawQueue.get(0) == DEBUG_CIRCLE)
                buildCircleMesh(vertices, indices);
            else
            {
                System.err.println("ERROR: Value not recognized as any debug type!");
                throw new IllegalArgumentException();
            }
        }

        vertexData = new float[vertices.size()];
        indexData = new int[indices.size()];

        for (int i = 0; i < vertexData.length; i++)
            vertexData[i] = vertices.get(i);

        for (int i = 0; i < indexData.length; i++)
            indexData[i] = indices.get(i);
    }

    private static void buildRayMesh(List<Float> vertices, List<Integer> indices)
    {
        float colA = drawQueue.remove(10);
        float colB = drawQueue.remove(9);
        float colG = drawQueue.remove(8);
        float colR = drawQueue.remove(7);

        float pos2z = drawQueue.remove(6);
        float pos2y = drawQueue.remove(5);
        float pos2x = drawQueue.remove(4);

        float pos1z = drawQueue.remove(3);
        float pos1y = drawQueue.remove(2);
        float pos1x = drawQueue.remove(1);

        drawQueue.remove(0);

        vertices.add(pos1x);
        vertices.add(pos1y);
        vertices.add(pos1z);

        vertices.add(colR);
        vertices.add(colG);
        vertices.add(colB);
        vertices.add(colA);

        vertices.add(pos1x + pos2x);
        vertices.add(pos1y + pos2y);
        vertices.add(pos1z + pos2z);

        vertices.add(colR);
        vertices.add(colG);
        vertices.add(colB);
        vertices.add(colA);

        int vertCount = (vertices.size() / 7) - 1;
        indices.add(vertCount - 1);
        indices.add(vertCount);
        indices.add(vertCount);
    }

    private static void buildCircleMesh(List<Float> vertices, List<Integer> indices)
    {
        float colA = drawQueue.remove(8);
        float colB = drawQueue.remove(7);
        float colG = drawQueue.remove(6);
        float colR = drawQueue.remove(5);

        float radius = drawQueue.remove(4);

        float posZ = drawQueue.remove(3);
        float posY = drawQueue.remove(2);
        float posX = drawQueue.remove(1);

        drawQueue.remove(0);

        int vertCount = vertices.size() / 7;
        float radStep = (float) Math.toRadians(360.0f / 16.0f);
        for (int i = 0; i < 16; i++)
        {
            vertices.add(posX + ((float) Math.sin(radStep * i) * radius));
            vertices.add(posY + ((float) Math.cos(radStep * i) * radius));
            vertices.add(posZ);

            vertices.add(colR);
            vertices.add(colG);
            vertices.add(colB);
            vertices.add(colA);

            if (i == 15)
            {
                indices.add(vertCount + i);
                indices.add(vertCount);
                indices.add(vertCount);
            }
            else
            {
                indices.add(vertCount + i);
                indices.add(vertCount + i + 1);
                indices.add(vertCount + i + 1);
            }
        }
    }

    private static void generateOpenGLObjects()
    {
        vboID = glGenBuffers();
        vaoID = glGenVertexArrays();
        eboID = glGenBuffers();

        glBindVertexArray(vaoID);
        glBindBuffer(GL_ARRAY_BUFFER, vboID);
        glBufferData(GL_ARRAY_BUFFER, vertexData, GL_STATIC_DRAW);
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, eboID);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, indexData, GL_STATIC_DRAW);
        glVertexAttribPointer(0, 3, GL_FLOAT, false, 7 * Float.BYTES, 0);
        glEnableVertexAttribArray(0);
        glVertexAttribPointer(1, 4, GL_FLOAT, false, 7 * Float.BYTES, 3 * Float.BYTES);
        glEnableVertexAttribArray(1);
    }

    private static boolean bindMaterial()
    {
        if (shader == null)
        {
            shader = Scene.debug2dShader;
            return false;
        }
        else
        {
            shader.bind();
            return true;
        }
    }

    private static void updateUniforms()
    {
        Matrix4f transform = new Matrix4f().identity();
        transform.translate(0.0f, 0.0f, 0.0f);
        transform.rotateXYZ(0.0f, 0.0f, 0.0f);
        transform.scale(1.0f, 1.0f, 1.0f);

        shader.bind();
        shader.updateUniforms(Color.WHITE, transform, Scene.mainCamera.projection, Scene.mainCamera.getTransformation());
    }

    private static void renderMesh()
    {
        glPolygonMode(GL_FRONT_AND_BACK, GL_LINE);
        glBindVertexArray(vaoID);
        glDrawElements(GL_TRIANGLES, indexData.length, GL_UNSIGNED_INT, 0);
        glBindVertexArray(0);
        glPolygonMode(GL_FRONT_AND_BACK, GL_FILL);
    }

    public static void render()
    {
        if (!bindMaterial())
            return;

        buildMeshFromDrawQueue();
        generateOpenGLObjects();
        updateUniforms();
        renderMesh();
    }
}