package engine.core;

public class GlobalSettings
{
    public final static String APPLICATION_NAME = "OpenGL 2d RPG Demo";
    public final static String GLSL_VERSION     = "#version 330";

    public static int windowWidth               = 1920;
    public static int windowHeight              = 1080;
    public static boolean useVsync              = true;
    public static boolean useLauncher           = false;
    public static boolean windowMaximized       = false;
    public static boolean windowBorderless      = false;
    public static boolean canBeResized          = false;
    public static int msaaSamples               = 1;

    // ImGui tool window flags
    public static boolean useImGuiStyleEditor   = false;
    public static boolean useImGuiAboutWindow   = false;
    public static boolean useImGuiDemoWindow    = false;
    public static boolean useImGuiUserGuide     = false;
    public static boolean useImGuiMetricsWindow = false;
}