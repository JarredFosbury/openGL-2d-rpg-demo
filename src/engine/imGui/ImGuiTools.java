package engine.imGui;

import engine.core.GlobalSettings;
import imgui.ImGui;

public class ImGuiTools extends ImGuiRootComponent
{
    public void render()
    {
        if (GlobalSettings.useImGuiStyleEditor)
        {
            ImGui.begin("Dear ImGui Style Editor");
            ImGui.showStyleEditor();
            ImGui.end();
        }

        if (GlobalSettings.useImGuiAboutWindow)
        {
            ImGui.showAboutWindow();
        }

        if (GlobalSettings.useImGuiDemoWindow)
        {
            ImGui.begin("Dear ImGui Demo");
            ImGui.showDemoWindow();
            ImGui.end();
        }

        if (GlobalSettings.useImGuiUserGuide)
        {
            ImGui.begin("Dear ImGui User Guide");
            ImGui.showUserGuide();
            ImGui.end();
        }

        if (GlobalSettings.useImGuiMetricsWindow)
        {
            ImGui.showMetricsWindow();
        }
    }
}