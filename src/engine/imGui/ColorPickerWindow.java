package engine.imGui;

import engine.core.Utils;
import imgui.ImGui;

public class ColorPickerWindow
{
    private float[] colorChannels;

    public ColorPickerWindow()
    {
        colorChannels = new float[3];
    }

    public void render()
    {
        ImGui.begin("Color Picker");
        ImGui.colorEdit3("Picker", colorChannels);
        if (ImGui.button("Print Color as Vector4f"))
        {
            System.out.println("new Vector4f(" +
                    Utils.round(colorChannels[0], 3) +
                    ", " + Utils.round(colorChannels[1], 3) +
                    ", " + Utils.round(colorChannels[2], 3) + ", 1.0f)");
        }
        ImGui.end();
    }
}