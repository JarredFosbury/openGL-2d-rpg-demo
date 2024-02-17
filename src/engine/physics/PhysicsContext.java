package engine.physics;

import java.util.ArrayList;
import java.util.List;

public class PhysicsContext
{
    public List<AxisAlignedBoundingBox> worldLayer;
    public List<AxisAlignedBoundingBox> propsLayer;
    public List<AxisAlignedBoundingBox> dynamicLayer;

    public PhysicsContext()
    {
        worldLayer = new ArrayList<>();
        propsLayer = new ArrayList<>();
        dynamicLayer = new ArrayList<>();
    }
}