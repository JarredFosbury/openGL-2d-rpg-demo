package engine.core;

import static org.lwjgl.glfw.GLFW.GLFW_PRESS;
import static org.lwjgl.glfw.GLFW.GLFW_RELEASE;

public class KeyListener
{
    private static final int NUM_KEYS   = 350;
    private static boolean keyPressed[] = new boolean[NUM_KEYS];
    private static int[] keyStates      = new int[NUM_KEYS];
    // 0 none, 1 pressed, 2 pressed latency frame, 3 active, 4 released, 5 released latency frame

    private KeyListener()
    {}

    public static void keyCallback(long windowPointer, int key, int scanCode, int action, int modifiers)
    {
        if (action == GLFW_PRESS)
        {
            KeyListener.keyPressed[key] = true;
            KeyListener.keyStates[key] = 1;
        }
        else if (action == GLFW_RELEASE)
        {
            KeyListener.keyPressed[key] = false;
            KeyListener.keyStates[key] = 4;
        }
    }

    public static boolean isKeyActive(int keyCode)
    {
        return KeyListener.keyPressed[keyCode];
    }

    public static boolean isKeyPressed(int keyCode)
    {
        return KeyListener.keyStates[keyCode] == 2;
    }

    public static boolean isKeyReleased(int keyCode)
    {
        return KeyListener.keyStates[keyCode] == 5;
    }

    public static void update()
    {
        for (int i = 0; i < KeyListener.NUM_KEYS; i++)
        {
            switch (KeyListener.keyStates[i])
            {
                case 1 -> KeyListener.keyStates[i] = 2;
                case 2 -> KeyListener.keyStates[i] = 3;
                case 4 -> KeyListener.keyStates[i] = 5;
                case 5 -> KeyListener.keyStates[i] = 0;
            }
        }
    }
}