package engine.core;

import org.joml.Vector2f;

import static org.lwjgl.glfw.GLFW.*;

public class MouseListener
{
    private static double scrollX, scrollY;
    private static double xPos, yPos, lastX, lastY;
    private static boolean isDragging;
    private static long windowPointer;

    private static final int NUM_MOUSE_BUTTONS  = 5;
    private static boolean[] activeMouseButtons = new boolean[NUM_MOUSE_BUTTONS];
    private static int[] mouseButtonState       = new int[NUM_MOUSE_BUTTONS];
    // 0 none, 1 pressed, 2 pressed latency frame, 3 active, 4 released, 5 released latency frame

    public static void update()
    {
        for (int i = 0; i < MouseListener.NUM_MOUSE_BUTTONS; i++)
        {
            switch (MouseListener.mouseButtonState[i])
            {
                case 1 -> MouseListener.mouseButtonState[i] = 2;
                case 2 -> MouseListener.mouseButtonState[i] = 3;
                case 4 -> MouseListener.mouseButtonState[i] = 5;
                case 5 -> MouseListener.mouseButtonState[i] = 0;
            }
        }
    }

    public static void mousePositionCallback(long windowPointer, double xPos, double yPos)
    {
        MouseListener.lastX = MouseListener.xPos;
        MouseListener.lastY = MouseListener.yPos;

        MouseListener.xPos = xPos;
        MouseListener.yPos = yPos;

        MouseListener.isDragging = MouseListener.activeMouseButtons[0] ||
                MouseListener.activeMouseButtons[1] || MouseListener.activeMouseButtons[2];
    }

    public static void mouseButtonCallback(long windowPointer, int button, int action, int modifiers)
    {
        if (button >= MouseListener.NUM_MOUSE_BUTTONS)
            return;

        if (action == GLFW_PRESS)
        {
            MouseListener.activeMouseButtons[button] = true;
            MouseListener.mouseButtonState[button] = 1;
        }
        else if (action == GLFW_RELEASE)
        {
            MouseListener.activeMouseButtons[button] = false;
            MouseListener.isDragging = false;

            MouseListener.mouseButtonState[button] = 4;
        }
    }

    public static void mouseScrollCallback(long windowPointer, double xOffset, double yOffset)
    {
        MouseListener.scrollX = xOffset;
        MouseListener.scrollY = yOffset;
    }

    public static void endFrame()
    {
        MouseListener.scrollX = 0;
        MouseListener.scrollY = 0;
        MouseListener.lastX = MouseListener.xPos;
        MouseListener.lastY = MouseListener.yPos;
    }

    public static float getPositionX() { return (float)MouseListener.xPos; }
    public static float getPositionY() { return (float)MouseListener.yPos; }
    public static Vector2f getPosition() { return new Vector2f((float)MouseListener.xPos, (float)MouseListener.yPos); }
    public static float getPositionDeltaX() { return (float)(MouseListener.lastX - MouseListener.xPos); }
    public static float getPositionDeltaY() { return (float)(MouseListener.lastY - MouseListener.yPos); }
    public static float getScrollX() { return (float)MouseListener.scrollX; }
    public static float getScrollY() { return (float)MouseListener.scrollY; }
    public static boolean isDragging() { return MouseListener.isDragging; }

    public static boolean isButtonActive(int button)
    {
        if (button < MouseListener.NUM_MOUSE_BUTTONS)
            return MouseListener.activeMouseButtons[button];
        else
            return false;
    }

    public static boolean isButtonPressed(int button)
    {
        if (button < MouseListener.NUM_MOUSE_BUTTONS)
            return MouseListener.mouseButtonState[button] == 2;
        else
            return false;
    }

    public static boolean isButtonReleased(int button)
    {
        if (button < MouseListener.NUM_MOUSE_BUTTONS)
            return MouseListener.mouseButtonState[button] == 5;
        else
            return false;
    }

    public static void setPosition(Vector2f inPos)
    {
        glfwSetCursorPos(MouseListener.windowPointer, inPos.x, inPos.y);
    }

    public static void setWindowPointer(long pointer)
    {
        MouseListener.windowPointer = pointer;
    }

    public static void setCursorMode(int modeIndex)
    {
        switch (modeIndex)
        {
            case 0 -> glfwSetInputMode(MouseListener.windowPointer, GLFW_CURSOR, GLFW_CURSOR_NORMAL);
            case 1 -> glfwSetInputMode(MouseListener.windowPointer, GLFW_CURSOR, GLFW_CURSOR_HIDDEN);
            case 2 -> glfwSetInputMode(MouseListener.windowPointer, GLFW_CURSOR, GLFW_CURSOR_DISABLED);
        }
    }
}