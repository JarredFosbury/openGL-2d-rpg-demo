package engine.core;

public class Time
{
    public static final double START_TIME = System.nanoTime();
    public static float fixedPhysicsTimeStep = 0.02f;
    public static float deltaTime;

    public static final long SECOND = 1000000000;

    public static float getTimeElapsed()
    {
        return (float)((System.nanoTime() - START_TIME) / SECOND);
    }
}