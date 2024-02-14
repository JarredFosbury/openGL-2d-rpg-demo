package engine.launcher;

import engine.core.MainComponent;

public class LauncherManager
{
    private static LauncherFrame window;

    public static void createJFrame()
    {
        window = new LauncherFrame(600, 450);
    }

    public static void launchGame()
    {
        window.dispose();
        MainComponent.launchGame();
    }
}