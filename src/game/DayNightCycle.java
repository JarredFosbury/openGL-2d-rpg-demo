package game;

import engine.core.Time;
import engine.core.Utils;
import org.joml.Vector4f;

public class DayNightCycle
{
    public Vector4f mainLightColor;
    public float totalCycleLength_MINS;
    public float cycleTimeElapsed_SECONDS;
    public float timeInGame_HOURS;
    public float timeInGame_MINS;

    public float dawnStartInGame_HOUR;
    public float dawnEndInGame_HOUR;
    public float duskStartInGame_HOUR;
    public float duskEndInGame_HOUR;

    public final Vector4f dayLightColor;
    public final Vector4f nightLightColor;

    private final static float ONE_MINUTE = 60.0f;

    public DayNightCycle()
    {
        dayLightColor = new Vector4f(0.96f, 0.9f, 0.86f, 1.0f);
        nightLightColor = new Vector4f(0.46f, 0.58f, 0.7f, 1.0f);
        mainLightColor = nightLightColor;
        totalCycleLength_MINS = 5.0f;
        cycleTimeElapsed_SECONDS = 0.0f;

        dawnStartInGame_HOUR = 4;
        dawnEndInGame_HOUR = 7;
        duskStartInGame_HOUR = 19;
        duskEndInGame_HOUR = 22;
    }

    public void setTime(int hour, int minute)
    {
        cycleTimeElapsed_SECONDS = ((totalCycleLength_MINS * ONE_MINUTE) / 24.0f) * hour + ((totalCycleLength_MINS * ONE_MINUTE) / 1440.0f) * minute;
    }

    public void update()
    {
        cycleTimeElapsed_SECONDS += Time.deltaTime;
        float cycleLength = totalCycleLength_MINS * ONE_MINUTE;

        if (cycleTimeElapsed_SECONDS >= cycleLength)
            cycleTimeElapsed_SECONDS -= cycleLength;

        timeInGame_HOURS = (float) Math.floor(cycleTimeElapsed_SECONDS / (cycleLength / 24.0f));
        timeInGame_MINS = (int) Math.floor(cycleTimeElapsed_SECONDS / (cycleLength / 1440.0f)) % 60;

        float currentTime_MINS = timeInGame_MINS + timeInGame_HOURS * ONE_MINUTE;
        if (currentTime_MINS < dawnEndInGame_HOUR * ONE_MINUTE && currentTime_MINS > dawnStartInGame_HOUR * ONE_MINUTE)
        {
            float interpolationWeight = (currentTime_MINS - (dawnStartInGame_HOUR * ONE_MINUTE)) / ((dawnEndInGame_HOUR - dawnStartInGame_HOUR) * ONE_MINUTE);
            mainLightColor = new Vector4f(
                    Utils.lerp(nightLightColor.x, dayLightColor.x, interpolationWeight),
                    Utils.lerp(nightLightColor.y, dayLightColor.y, interpolationWeight),
                    Utils.lerp(nightLightColor.z, dayLightColor.z, interpolationWeight),
                    1.0f
            );
        }

        if (currentTime_MINS < duskEndInGame_HOUR * ONE_MINUTE && currentTime_MINS > duskStartInGame_HOUR * ONE_MINUTE)
        {
            float interpolationWeight = (currentTime_MINS - (duskStartInGame_HOUR * ONE_MINUTE)) / ((duskEndInGame_HOUR - duskStartInGame_HOUR) * ONE_MINUTE);
            mainLightColor = new Vector4f(
                    Utils.lerp(dayLightColor.x, nightLightColor.x, interpolationWeight),
                    Utils.lerp(dayLightColor.y, nightLightColor.y, interpolationWeight),
                    Utils.lerp(dayLightColor.z, nightLightColor.z, interpolationWeight),
                    1.0f
            );
        }
    }
}