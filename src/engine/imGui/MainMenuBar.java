package engine.imGui;

import engine.core.GlobalSettings;
import engine.core.Scene;
import engine.core.Window;
import imgui.ImGui;

public class MainMenuBar extends ImGuiRootComponent
{
    public MainMenuBar()
    {
        super("main_menu_bar");
    }

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

        if (ImGui.beginMenu("ImGui Tools"))
        {
            if (ImGui.menuItem("Style Editor"))
                GlobalSettings.useImGuiStyleEditor = !GlobalSettings.useImGuiStyleEditor;

            if (ImGui.menuItem("About"))
                GlobalSettings.useImGuiAboutWindow = !GlobalSettings.useImGuiAboutWindow;

            if (ImGui.menuItem("Demo"))
                GlobalSettings.useImGuiDemoWindow = !GlobalSettings.useImGuiDemoWindow;

            if (ImGui.menuItem("User Guide"))
                GlobalSettings.useImGuiUserGuide = !GlobalSettings.useImGuiUserGuide;

            if (ImGui.menuItem("Metrics"))
                GlobalSettings.useImGuiMetricsWindow = !GlobalSettings.useImGuiMetricsWindow;

            ImGui.endMenu();
        }

        if (ImGui.beginMenu("Editor Tools"))
        {
            if (ImGui.menuItem("Color Picker"))
                ((ColorPickerWindow) Scene.findImGuiComponents("color_picker_window")[0]).isActive = true;

            ImGui.endMenu();
        }

        ImGui.endMainMenuBar();
    }
}