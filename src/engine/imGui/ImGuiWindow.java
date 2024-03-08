package engine.imGui;

import imgui.ImGui;

public class ImGuiWindow extends ImGuiRootComponent
{
    private final String windowName;
    private String title;

    public ImGuiWindow(String windowName, String title)
    {
        this.windowName = windowName;
        this.title = title;
    }

    public void render()
    {
        ImGui.begin(title + "###" + windowName);
        renderWindowContents();
        ImGui.end();
    }

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