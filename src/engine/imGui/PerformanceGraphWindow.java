package engine.imGui;

import engine.core.Time;
import engine.core.Utils;
import imgui.ImGui;

public class PerformanceGraphWindow extends ImGuiWindow
{
    private final Runtime runtime;
    private final double value_MB;

    private float[] frameTimes;
    private float lastFrameTime;
    private float refreshCycleTimer;
    private long totalMemory_BYTES;
    private long freeMemory_BYTES;
    private long allocatedMemory_BYTES;

    public PerformanceGraphWindow()
    {
        super("performance_metrics_window", "Performance Metrics", false);
        runtime = Runtime.getRuntime();
        value_MB = 1024.0 * 1024.0;

        frameTimes = new float[1000];
        totalMemory_BYTES = runtime.totalMemory();
        freeMemory_BYTES = runtime.freeMemory();
        allocatedMemory_BYTES = totalMemory_BYTES - freeMemory_BYTES;
    }

    public void hotkeyControls()
    {}

    public void renderWindowContents()
    {
        getNewFrameTimes();

        refreshCycleTimer -= Time.deltaTime;
        if (refreshCycleTimer <= 0.0f)
        {
            refreshCycleTimer += 1.0f;
            fetchNewValues();
        }

        ImGui.newLine();
        ImGui.text("Frame Rate Statistics");
        ImGui.labelText("Frames per Second", String.valueOf(Utils.round(1.0f / (lastFrameTime / 1000.0f), 1)));
        ImGui.plotHistogram("Frame time (" + lastFrameTime + "ms)",
                frameTimes, frameTimes.length, -1, "", 0.0f, 16.0f);

        ImGui.newLine();
        ImGui.text("Memory Statistics (Measured in Megabytes)");
        ImGui.labelText("Total Memory", String.valueOf(Utils.round(totalMemory_BYTES / value_MB, 3)));
        ImGui.labelText("Free Memory", String.valueOf(Utils.round(freeMemory_BYTES / value_MB, 3)));
        ImGui.labelText("Allocated Memory", String.valueOf(Utils.round(allocatedMemory_BYTES / value_MB, 3)));

        ImGui.newLine();
        ImGui.text("Geometry Statistics");
        ImGui.text("TODO");
    }

    private void getNewFrameTimes()
    {
        for (int i = 0; i < frameTimes.length - 1; i++)
            frameTimes[i] = frameTimes[i + 1];

        frameTimes[frameTimes.length - 1] = 1000.0f * Time.deltaTime;
    }

    private void fetchNewValues()
    {
        lastFrameTime = Utils.round(frameTimes[frameTimes.length - 1], 2);
        totalMemory_BYTES = runtime.totalMemory();
        freeMemory_BYTES = runtime.freeMemory();
        allocatedMemory_BYTES = totalMemory_BYTES - freeMemory_BYTES;
    }
}
