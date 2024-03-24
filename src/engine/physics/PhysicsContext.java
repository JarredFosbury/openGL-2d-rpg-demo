package engine.physics;

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
}