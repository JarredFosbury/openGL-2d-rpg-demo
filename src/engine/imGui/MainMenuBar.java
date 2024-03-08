package engine.imGui;

import engine.core.Window;
import imgui.ImGui;

public class MainMenuBar extends ImGuiRootComponent
{
    public MainMenuBar()
    {}

    public void render()
    {
        ImGui.beginMainMenuBar();

        if (ImGui.beginMenu("File"))
        {
            if (ImGui.menuItem("Close Application"))
            {
                Window.get().queueWindowTermination();
            }

            ImGui.endMenu();
        }

        ImGui.endMainMenuBar();
    }
}