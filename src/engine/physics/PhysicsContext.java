package engine.physics;

import org.joml.Vector3f;

import java.util.ArrayList;

public class PhysicsContext
{
    public ArrayList<String> layerMasks;
    public ArrayList<ColliderAABB> alignedColliders;

    public PhysicsContext()
    {
        resetToDefaultValues();
    }

    public boolean compareLayerMaskIndex(int index, int index2)
    {
        return layerMasks.get(index).equals(layerMasks.get(index2));
    }

    public void resetToDefaultValues()
    {
        layerMasks = new ArrayList<>();
        alignedColliders = new ArrayList<>();
        layerMasks.add("Default");
    }

    public Vector3f collideWithLayer(ColliderAABB colliderRef, int targetLayerIndex)
    {
        Vector3f collisionSolutionSum = new Vector3f(0.0f);
        for (ColliderAABB alignedCollider : alignedColliders)
            if (alignedCollider.layerMaskIndex == targetLayerIndex)
                collisionSolutionSum.add(colliderRef.collide(alignedCollider));

        return collisionSolutionSum;
    }

    public boolean rayAABBIntersection(ColliderAABB box, Ray ray)
    {
        float[] min = {box.position.x - box.scale.x/2.0f, box.position.y - box.scale.y/2.0f};
        float[] max = {box.position.x + box.scale.x/2.0f, box.position.y + box.scale.y/2.0f};
        float[] origin = {ray.position.x, ray.position.y};
        float[] dir_inv = {ray.inverseDirection.x, ray.inverseDirection.y};
        float tmin = Float.MIN_VALUE, tmax = Float.MAX_VALUE;

        // x test
        float axisMin = (min[0] - origin[0]) * dir_inv[0];
        float axisMax = (max[0] - origin[0]) * dir_inv[0];
        tmin = Math.max(tmin, Math.min(axisMin, axisMax));
        tmax = Math.min(tmax, Math.max(axisMin, axisMax));

        // y test
        axisMin = (min[1] - origin[1]) * dir_inv[1];
        axisMax = (max[1] - origin[1]) * dir_inv[1];
        tmin = Math.max(tmin, Math.min(axisMin, axisMax));
        tmax = Math.min(tmax, Math.max(axisMin, axisMax));

        return tmax > Math.max(tmin, 0.0) && new Vector3f(ray.direction).mul(tmin).length() <= ray.direction.length();
    }

    public boolean rayLayerIntersection(int targetLayerIndex, Ray ray)
    {
        for (ColliderAABB alignedCollider : alignedColliders)
            if (alignedCollider.layerMaskIndex == targetLayerIndex)
                if (rayAABBIntersection(alignedCollider, ray))
                    return true;

        return false;
    }
}