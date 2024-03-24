package engine.physics;

import java.util.ArrayList;
import java.util.List;

public class PhysicsContext
{
    public List<ColliderAABB> worldLayer;
    public List<ColliderAABB> propsLayer;
    public List<ColliderAABB> dynamicLayer;

    public PhysicsContext()
    {
        worldLayer = new ArrayList<>();
        propsLayer = new ArrayList<>();
        dynamicLayer = new ArrayList<>();
    }
}