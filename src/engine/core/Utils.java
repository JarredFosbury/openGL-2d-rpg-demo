package engine.core;

import java.util.concurrent.ThreadLocalRandom;

public class Utils
{
    public static float round(float value, int decimalPlace)
    {
        float multiplier = (float)Math.pow(10.0, decimalPlace);
        return Math.round(value * multiplier) / multiplier;
    }

    public static double round(double value, int decimalPlace)
    {
        double multiplier = Math.pow(10.0, decimalPlace);
        return Math.round(value * multiplier) / multiplier;
    }

    public static float lerp(float a, float b, float weight)
    {
        return a + (b - a) * weight;
    }

    public static int randomRangeInteger(int min, int max)
    {
        return ThreadLocalRandom.current().nextInt(min, max + 1);
    }

    public static float randomRangeFloat(float min, float max)
    {
        return min + ThreadLocalRandom.current().nextFloat() * (max - min);
    }

    public static float clamp01(float in)
    {
        if (in > 1.0f)
            return 1.0f;
        else
            return Math.max(in, 0.0f);
    }

    public static int clampInt(int value, int min, int max)
    {
        if (value > max)
            return max;

        return Math.max(value, min);
    }
}