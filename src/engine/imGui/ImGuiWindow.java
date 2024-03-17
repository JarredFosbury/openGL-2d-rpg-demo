package engine.imGui;

import imgui.ImGui;

public class ImGuiWindow extends ImGuiRootComponent
{
    private final String windowName;
    private String title;

    public ImGuiWindow(String windowName, String title)
    {
        super(windowName);
        this.windowName = windowName;
        this.title = title;
    }

    public void render()
    {
        ImGui.begin(title + "###" + windowName);

        if (ImGuiWindowFocusManager.currentFocusedWindow.equals(windowName))
            hotkeyControls();

        renderWindowContents();

        if (ImGui.isWindowFocused())
            ImGuiWindowFocusManager.currentFocusedWindow = windowName;

        ImGui.end();
    }

    public void hotkeyControls()
    {}

    public void renderWindowContents()
    {}

    public String getWindowName()
    {
        return windowName;
    }

    public String getTitle()
    {
        return title;
    }

    public void setTitle(String title)
    {
        this.title = title;
    }
}