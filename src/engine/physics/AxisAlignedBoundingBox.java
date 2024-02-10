package engine.physics;

import engine.rendering.Color;
import engine.rendering.Sprite;
import org.joml.Vector2f;
import org.joml.Vector2i;
import org.joml.Vector3f;

import static org.lwjgl.opengl.GL11.*;

public class AxisAlignedBoundingBox
{
    public Vector3f position;
    public Vector3f dimensions;

    private final Sprite sprite;

    public AxisAlignedBoundingBox()
    {
        position = new Vector3f(0.0f);
        dimensions = new Vector3f(1.0f);
        sprite = new Sprite("res/textures/square.png", Color.GREEN);
    }

    public Vector3f collide(AxisAlignedBoundingBox aabb)
    {
        Vector3f aabbHalfSize = new Vector3f(aabb.dimensions).mul(0.5f);
        Vector2f aabb_xMinMax = new Vector2f(aabb.position.x - aabbHalfSize.x, aabb.position.x + aabbHalfSize.x);
        Vector2f aabb_yMinMax = new Vector2f(aabb.position.y - aabbHalfSize.y, aabb.position.y + aabbHalfSize.y);
        Vector2f aabb_zMinMax = new Vector2f(aabb.position.z - aabbHalfSize.z, aabb.position.z + aabbHalfSize.z);

        Vector3f thisHalfSize = new Vector3f(dimensions).mul(0.5f);
        Vector2f this_xMinMax = new Vector2f(position.x - thisHalfSize.x, position.x + thisHalfSize.x);
        Vector2f this_yMinMax = new Vector2f(position.y - thisHalfSize.y, position.y + thisHalfSize.y);
        Vector2f this_zMinMax = new Vector2f(position.z - thisHalfSize.z, position.z + thisHalfSize.z);

        if (this_xMinMax.x <= aabb_xMinMax.y &&
                this_xMinMax.y >= aabb_xMinMax.x &&
                this_yMinMax.x <= aabb_yMinMax.y &&
                this_yMinMax.y >= aabb_yMinMax.x &&
                this_zMinMax.x <= aabb_zMinMax.y &&
                this_zMinMax.y >= aabb_zMinMax.x)
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

            Vector3f dimensionSums = new Vector3f(aabbHalfSize.x + thisHalfSize.x, aabbHalfSize.y + thisHalfSize.y, aabbHalfSize.z + thisHalfSize.z);
            Vector3f percentagesPerAxis = new Vector3f(
                    Math.abs(colliderToThis.x) / dimensionSums.x,
                    Math.abs(colliderToThis.y) / dimensionSums.y,
                    Math.abs(colliderToThis.z) / dimensionSums.z);

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

    public void renderColliderSprite()
    {
        glPolygonMode(GL_FRONT_AND_BACK, GL_LINE);
        sprite.position = this.position;
        sprite.scale = this.dimensions;
        sprite.render();
        glPolygonMode(GL_FRONT_AND_BACK, GL_FILL);
    }
}