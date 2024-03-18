package engine.imGui;

import imgui.ImGui;

public class ImGuiWindow extends ImGuiRootComponent
{
    public boolean isActive;

    private final String windowName;
    private String title;
    private final boolean alwaysActive;

    public ImGuiWindow(String windowName, String title, boolean alwaysActive)
    {
        super(windowName);
        this.windowName = windowName;
        this.title = title;
        this.alwaysActive = alwaysActive;
        this.isActive = alwaysActive;
    }

    public void render()
    {
        if (!isActive)
            return;

        ImGui.begin(title + "###" + windowName);
        if (ImGuiWindowFocusManager.currentFocusedWindow.equals(windowName))
            hotkeyControls();

        if (!alwaysActive)
            if (ImGui.button("Close This Window"))
                this.isActive = false;

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