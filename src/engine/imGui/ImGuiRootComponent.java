package engine.imGui;

import engine.core.Scene;

public class ImGuiRootComponent
{
    public final String NAME;

    public ImGuiRootComponent(String name)
    {
        NAME = name;
        Scene.imGuiComponents.add(this);
    }

    public void render()
    {}

    public void delete()
    {
        Scene.imGuiComponents.remove(this);
    }
}