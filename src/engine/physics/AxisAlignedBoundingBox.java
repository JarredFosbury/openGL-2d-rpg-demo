package engine.physics;

import org.joml.Vector2f;
import org.joml.Vector2i;
import org.joml.Vector3f;

public class AxisAlignedBoundingBox
{
    public Vector3f position;
    public Vector3f dimensions;

    public AxisAlignedBoundingBox()
    {
        position = new Vector3f(0.0f);
        dimensions = new Vector3f(1.0f);
    }

    public Vector3f collide(AxisAlignedBoundingBox aabb)
    {
        Vector3f aabbHalfSize = new Vector3f(aabb.dimensions).mul(0.5f);
        Vector2f aabb_xMinMax = new Vector2f(aabb.position.x - aabbHalfSize.x, aabb.position.x + aabbHalfSize.x);
        Vector2f aabb_yMinMax = new Vector2f(aabb.position.y - aabbHalfSize.y, aabb.position.y + aabbHalfSize.y);

        Vector3f thisHalfSize = new Vector3f(dimensions).mul(0.5f);
        Vector2f this_xMinMax = new Vector2f(position.x - thisHalfSize.x, position.x + thisHalfSize.x);
        Vector2f this_yMinMax = new Vector2f(position.y - thisHalfSize.y, position.y + thisHalfSize.y);

        if (this_xMinMax.x <= aabb_xMinMax.y &&
                this_xMinMax.y >= aabb_xMinMax.x &&
                this_yMinMax.x <= aabb_yMinMax.y &&
                this_yMinMax.y >= aabb_yMinMax.x)
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
}