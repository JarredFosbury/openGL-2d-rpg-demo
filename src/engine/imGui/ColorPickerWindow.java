package engine.imGui;

import engine.core.Utils;
import imgui.ImGui;

public class ColorPickerWindow extends ImGuiWindow
{
    private float[] colorChannels;

    public ColorPickerWindow()
    {
        super("color_picker_window", "Color Picker");
        colorChannels = new float[3];
    }

    public void renderWindowContents()
    {
        ImGui.colorEdit3("Picker", colorChannels);
        if (ImGui.button("Print Color as Vector4f"))
        {
            System.out.println("new Vector4f(" +
                    Utils.round(colorChannels[0], 3) +
                    "f, " + Utils.round(colorChannels[1], 3) +
                    "f, " + Utils.round(colorChannels[2], 3) + "f, 1.0f)");
        }
    }
}