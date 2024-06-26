package engine.launcher;

import engine.core.GlobalSettings;
import engine.core.MainComponent;

public class LauncherManager
{
    private static LauncherFrame window;

    public static void createJFrame()
    {
        window = new LauncherFrame(350, 315);
    }

    public static void launchGame()
    {
        int[] resolution = window.getSelectedResolution();
        GlobalSettings.windowWidth = resolution[0];
        GlobalSettings.windowHeight = resolution[1];

        boolean[] windowSettings = window.getWindowSettings();
        GlobalSettings.useVsync = windowSettings[0];
        GlobalSettings.windowMaximized = windowSettings[1];
        GlobalSettings.windowBorderless = windowSettings[2];

        GlobalSettings.msaaSamples = 2 ^ window.getMSAA();

        window.dispose();
        MainComponent.launchGame();
    }
}