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

    public final Vector4f DAY_LIGHT_COLOR;
    public final Vector4f NIGHT_LIGHT_COLOR;

    private final static float ONE_MINUTE = 60.0f;

    public DayNightCycle()
    {
        DAY_LIGHT_COLOR = new Vector4f(0.96f, 0.9f, 0.86f, 1.0f);
        NIGHT_LIGHT_COLOR = new Vector4f(0.31f, 0.396f, 0.592f, 1.0f);
        mainLightColor = NIGHT_LIGHT_COLOR;
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

    public String getTimeFormatted()
    {
        String hours = (timeInGame_HOURS <= 9 ? "0" : "") + (int) timeInGame_HOURS + ": ";
        String mins = (timeInGame_MINS <= 9 ? "0" : "") + (int) timeInGame_MINS;
        return hours + mins;
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
                    Utils.lerp(NIGHT_LIGHT_COLOR.x, DAY_LIGHT_COLOR.x, interpolationWeight),
                    Utils.lerp(NIGHT_LIGHT_COLOR.y, DAY_LIGHT_COLOR.y, interpolationWeight),
                    Utils.lerp(NIGHT_LIGHT_COLOR.z, DAY_LIGHT_COLOR.z, interpolationWeight),
                    1.0f
            );
        }

        if (currentTime_MINS < duskEndInGame_HOUR * ONE_MINUTE && currentTime_MINS > duskStartInGame_HOUR * ONE_MINUTE)
        {
            float interpolationWeight = (currentTime_MINS - (duskStartInGame_HOUR * ONE_MINUTE)) / ((duskEndInGame_HOUR - duskStartInGame_HOUR) * ONE_MINUTE);
            mainLightColor = new Vector4f(
                    Utils.lerp(DAY_LIGHT_COLOR.x, NIGHT_LIGHT_COLOR.x, interpolationWeight),
                    Utils.lerp(DAY_LIGHT_COLOR.y, NIGHT_LIGHT_COLOR.y, interpolationWeight),
                    Utils.lerp(DAY_LIGHT_COLOR.z, NIGHT_LIGHT_COLOR.z, interpolationWeight),
                    1.0f
            );
        }
    }
}