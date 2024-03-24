package engine.physics;

import engine.core.Entity;
import engine.core.EntityType;
import engine.core.Scene;
import engine.shaders.Debug2dShader;
import org.joml.*;

import java.lang.Math;

import static org.lwjgl.opengl.GL11.GL_FLOAT;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;

public class ColliderAABB extends Entity
{
    public boolean isTrigger;
    public Vector4f debugRenderColor;
    public int layerMaskIndex;

    private final float[] vertexData = {
            // positions
            0.5f,  0.5f, 0.0f,      // top right
            0.5f, -0.5f, 0.0f,      // bottom right
            -0.5f, -0.5f, 0.0f,     // bottom left
            -0.5f,  0.5f, 0.0f,     // top left
    };

    private final int[] indices = {
            0, 1, 1,
            1, 2, 2,
            2, 3, 3,
            3, 0, 0
    };

    private int vboID, vaoID, eboID;

    private final Debug2dShader shader;

    public ColliderAABB(String name, int HIERARCHY_INDEX, Vector4f color)
    {
        super(name, EntityType.ColliderAABB, HIERARCHY_INDEX);
        this.isTrigger = false;
        this.shader = Scene.debug2dShader;
        this.debugRenderColor = color;
        this.layerMaskIndex = 0;
        Scene.physics.alignedColliders.add(this);
        initDebugMeshData();
    }

    private void initDebugMeshData()
    {
        vboID = glGenBuffers();
        vaoID = glGenVertexArrays();
        eboID = glGenBuffers();

        glBindVertexArray(vaoID);
        glBindBuffer(GL_ARRAY_BUFFER, vboID);
        glBufferData(GL_ARRAY_BUFFER, vertexData, GL_STATIC_DRAW);
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, eboID);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, indices, GL_STATIC_DRAW);
        glVertexAttribPointer(0, 3, GL_FLOAT, false, 3 * Float.BYTES, 0);
        glEnableVertexAttribArray(0);
    }

    public Vector3f collide(ColliderAABB aabb)
    {
        Vector3f aabbHalfSize = new Vector3f(aabb.scale).mul(0.5f);
        Vector2f aabb_xMinMax = new Vector2f(aabb.position.x - aabbHalfSize.x, aabb.position.x + aabbHalfSize.x);
        Vector2f aabb_yMinMax = new Vector2f(aabb.position.y - aabbHalfSize.y, aabb.position.y + aabbHalfSize.y);

        Vector3f thisHalfSize = new Vector3f(scale).mul(0.5f);
        Vector2f this_xMinMax = new Vector2f(position.x - thisHalfSize.x, position.x + thisHalfSize.x);
        Vector2f this_yMinMax = new Vector2f(position.y - thisHalfSize.y, position.y + thisHalfSize.y);

        if (this_xMinMax.x <= aabb_xMinMax.y && this_xMinMax.y >= aabb_xMinMax.x &&
                this_yMinMax.x <= aabb_yMinMax.y && this_yMinMax.y >= aabb_yMinMax.x)
        {
            Vector3f colliderToThis = new Vector3f(position).sub(aabb.position);
            Vector2i axisSign = new Vector2i(0);
            if (colliderToThis.x > 0)
                axisSign.x = 1;
            else if (colliderToThis.x < 0)
                axisSign.x = -1;

            if (colliderToThis.y > 0)
                axisSign.y = 1;
            else if (colliderToThis.y < 0)
                axisSign.y = -1;

            Vector2f dimensionSums = new Vector2f(aabbHalfSize.x + thisHalfSize.x, aabbHalfSize.y + thisHalfSize.y);
            Vector2f percentagesPerAxis = new Vector2f(
                    Math.abs(colliderToThis.x) / dimensionSums.x,
                    Math.abs(colliderToThis.y) / dimensionSums.y);

            float xDifferential = (1.0f - percentagesPerAxis.x) * dimensionSums.x;
            float yDifferential = (1.0f - percentagesPerAxis.y) * dimensionSums.y;
            if (xDifferential < yDifferential)
                colliderToThis = new Vector3f(xDifferential * (float) axisSign.x, 0.0f, 0.0f);
            else
                colliderToThis = new Vector3f(0.0f, yDifferential * (float) axisSign.y, 0.0f);

            return new Vector3f(colliderToThis);
        }
        else
        {
            return new Vector3f(0.0f);
        }
    }

    public void renderDebug()
    {
        glPolygonMode(GL_FRONT_AND_BACK, GL_LINE);
        Matrix4f transform = new Matrix4f().identity();
        transform.translate(position);
        transform.rotateXYZ(rotation);
        transform.scale(scale);

        shader.bind();
        shader.updateUniforms(debugRenderColor, transform, Scene.mainCamera.projection, Scene.mainCamera.getTransformation());
        glBindVertexArray(vaoID);
        glDrawElements(GL_TRIANGLES, indices.length, GL_UNSIGNED_INT, 0);
        glBindVertexArray(0);
        shader.unbind();
        glPolygonMode(GL_FRONT_AND_BACK, GL_FILL);
    }
}