package engine.core;

import engine.launcher.LauncherManager;

public class MainComponent
{
    public static void main(String[] args)
    {
        if (GlobalSettings.useLauncher)
            LauncherManager.createJFrame();
        else
            launchGame();
    }

    public static void launchGame()
    {
        Window window = Window.get();
        window.run();
    }
}