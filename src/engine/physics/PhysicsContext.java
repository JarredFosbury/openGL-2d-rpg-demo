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
        float[] dir_inv = {ray.direction.x * -1.0f, ray.direction.y * -1.0f};
        float tmin = Float.MIN_VALUE, tmax = Float.MAX_VALUE;

        for (int i = 0; i < 2; ++i)
        {
            float t1 = (min[i] - origin[i])*dir_inv[i];
            float t2 = (max[i] - origin[i])*dir_inv[i];
            tmin = Math.max(tmin, Math.min(t1, t2));
            tmax = Math.min(tmax, Math.max(t1, t2));
        }

        return tmax > Math.max(tmin, 0.0);
    }
}