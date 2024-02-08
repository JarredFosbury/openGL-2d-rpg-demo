package core;

public class GlobalSettings
{
    public final static String APPLICATION_NAME = "OpenGL Game Engine";
    public final static String GLSL_VERSION     = "#version 330";
    public final static int WINDOW_WIDTH        = 2560;
    public final static int WINDOW_HEIGHT       = 1440;
    public final static boolean USE_VSYNC       = true;

    public static boolean windowMaximized       = true;
    public static boolean windowBorderless      = true;
    public static int msaaSamples               = 4;
    public static float fixedPhysicsTimeStep    = 0.02f;
}