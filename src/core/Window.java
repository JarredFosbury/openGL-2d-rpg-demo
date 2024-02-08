package core;

import imgui.ImGui;
import imgui.ImGuiIO;
import imgui.flag.ImGuiConfigFlags;
import imgui.gl3.ImGuiImplGl3;
import imgui.glfw.ImGuiImplGlfw;
import org.lwjgl.glfw.Callbacks;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.opengl.GL;

import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryUtil.NULL;

public class Window
{
    private static Window window = null;

    private long glfwWindowPtr;
    private boolean windowTerminationQueued;

    private final ImGuiImplGlfw imGuiGlfw   = new ImGuiImplGlfw();
    private final ImGuiImplGl3 imGuiGl3     = new ImGuiImplGl3();

    private Window()
    {
        this.windowTerminationQueued = false;
    }

    public static Window get()
    {
        if (Window.window == null)
            Window.window = new Window();

        return Window.window;
    }

    public void run()
    {
        init();
        loop();

        // free memory
        glfwFreeCallbacks(glfwWindowPtr);
        glfwDestroyWindow(glfwWindowPtr);

        // terminate glfw and imGui and free error callback
        imGuiGlfw.dispose();
        imGuiGl3.dispose();
        ImGui.destroyContext();
        Callbacks.glfwFreeCallbacks(glfwWindowPtr);
        glfwTerminate();
    }

    public void init()
    {
        // setup error callback
        GLFWErrorCallback.createPrint(System.err).set();

        // initialize glfw
        if (!glfwInit())
            throw new IllegalStateException("Unable to initialize GLFW");

        // configure glfw
        glfwDefaultWindowHints();
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE);
        glfwWindowHint(GLFW_MAXIMIZED, GlobalSettings.windowMaximized ? GLFW_TRUE : GLFW_FALSE);
        glfwWindowHint(GLFW_DECORATED, GlobalSettings.windowBorderless ? GLFW_FALSE : GLFW_TRUE);
        glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3);
        glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 3);
        glfwWindowHint(GLFW_SAMPLES, GlobalSettings.msaaSamples);

        // create window
        glfwWindowPtr = glfwCreateWindow(GlobalSettings.WINDOW_WIDTH, GlobalSettings.WINDOW_HEIGHT,
                GlobalSettings.APPLICATION_NAME, NULL, NULL);

        if (glfwWindowPtr == NULL)
            throw new IllegalStateException("Failed to create the GLFW window!");

        // assign mouse and key callbacks in glfw
        glfwSetCursorPosCallback(glfwWindowPtr, MouseListener::mousePositionCallback);
        glfwSetMouseButtonCallback(glfwWindowPtr, MouseListener::mouseButtonCallback);
        glfwSetScrollCallback(glfwWindowPtr, MouseListener::mouseScrollCallback);
        glfwSetKeyCallback(glfwWindowPtr, KeyListener::keyCallback);
        MouseListener.setWindowPointer(glfwWindowPtr);

        // make openGL context current
        glfwMakeContextCurrent(glfwWindowPtr);

        // enable v-sync
        glfwSwapInterval(GlobalSettings.USE_VSYNC ? 1 : 0);

        // show the window
        glfwShowWindow(glfwWindowPtr);

        // critical for using OpenGL bindings set up by GLFW with LWJGL
        GL.createCapabilities();

        imGuiInit();
        Scene.initialize();
    }

    private void imGuiInit()
    {
        ImGui.createContext();
        ImGuiIO io = ImGui.getIO();
        //io.addConfigFlags(ImGuiConfigFlags.ViewportsEnable);

        imGuiGlfw.init(glfwWindowPtr, true);
        imGuiGl3.init(GlobalSettings.GLSL_VERSION);
    }

    public void loop()
    {
        double lastTime = System.nanoTime();
        float physicsStepTime = 0.0f;

        while (!glfwWindowShouldClose(glfwWindowPtr))
        {
            // basic performance statistics
            Time.deltaTime = (float) ((System.nanoTime() - lastTime) / Time.SECOND);
            physicsStepTime += Time.deltaTime;
            lastTime = System.nanoTime();

            // poll input events
            Scene.pollInput();
            glfwPollEvents();

            MouseListener.update();
            KeyListener.update();
            Scene.update();

            if (physicsStepTime >= Time.fixedPhysicsTimeStep)
            {
                physicsStepTime -= Time.fixedPhysicsTimeStep;
                Scene.fixedPhysicsUpdate();
            }

            // clear window to color and swap buffers
            glClearColor(0.05f, 0.18f, 0.3f, 1.0f);
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

            // render out to the scene
            Scene.render();

            // render ImGui
            imGuiGlfw.newFrame();
            ImGui.newFrame();

            // TODO: imGui render call

            ImGui.render();
            imGuiGl3.renderDrawData(ImGui.getDrawData());

            if (ImGui.getIO().hasConfigFlags(ImGuiConfigFlags.ViewportsEnable))
            {
                final long backupWindowPtr = GLFW.glfwGetCurrentContext();
                ImGui.updatePlatformWindows();
                ImGui.renderPlatformWindowsDefault();
                GLFW.glfwMakeContextCurrent(backupWindowPtr);
            }

            // swap buffers to display to screen
            glfwSwapBuffers(glfwWindowPtr);

            if (windowTerminationQueued)
                terminateCurrentWindow();

            MouseListener.endFrame();
        }
    }

    public int[] getWindowSize()
    {
        int[] widthBuffer = new int[1];
        int[] heightBuffer = new int[1];

        glfwGetWindowSize(glfwWindowPtr, widthBuffer, heightBuffer);

        return new int[] {widthBuffer[0], heightBuffer[0]};
    }

    public void queueWindowTermination()
    {
        windowTerminationQueued = true;
    }

    private void terminateCurrentWindow()
    {
        glfwSetWindowShouldClose(glfwWindowPtr, true);
    }

    public long getGlfwWindowPtr()
    {
        return glfwWindowPtr;
    }
}