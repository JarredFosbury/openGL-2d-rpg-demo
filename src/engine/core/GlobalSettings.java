package engine.core;

public class GlobalSettings
{
    public final static String APPLICATION_NAME = "OpenGL Game Engine";
    public final static String GLSL_VERSION     = "#version 330";
    public final static int WINDOW_WIDTH        = 1920;
    public final static int WINDOW_HEIGHT       = 1080;
    public final static boolean USE_VSYNC       = true;

    public static boolean windowMaximized       = false;
    public static boolean windowBorderless      = false;
    public static int msaaSamples               = 4;
}