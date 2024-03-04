package engine.imGui;

import engine.core.Scene;

public class ImGuiWindow
{
    public ImGuiWindow()
    {
        Scene.imGuiWindows.add(this);
    }

    public void render()
    {}
}