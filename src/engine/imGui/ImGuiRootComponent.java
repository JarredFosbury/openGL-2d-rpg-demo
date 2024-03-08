package engine.imGui;

import engine.core.Scene;

public class ImGuiRootComponent
{
    public ImGuiRootComponent()
    {
        Scene.imGuiComponents.add(this);
    }

    public void render()
    {}
}