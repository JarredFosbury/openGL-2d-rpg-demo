package engine.audio;

import org.lwjgl.openal.AL;
import org.lwjgl.openal.ALC;
import org.lwjgl.openal.ALCCapabilities;
import org.lwjgl.openal.ALCapabilities;

import static org.lwjgl.openal.ALC10.*;

public class OpenALTestImplementation
{
    private static long audioContext;
    private static long audioDevice;

    public static void main(String[] args)
    {
        initOpenAL();
    }

    private static void initOpenAL()
    {
        String defaultDeviceName = alcGetString(0, ALC_DEFAULT_DEVICE_SPECIFIER);
        audioDevice = alcOpenDevice(defaultDeviceName);

        int[] attributes = {0};
        audioContext = alcCreateContext(audioContext, attributes);
        alcMakeContextCurrent(audioContext);

        ALCCapabilities alcCapabilities = ALC.createCapabilities(audioDevice);
        ALCapabilities alCapabilities = AL.createCapabilities(alcCapabilities);

        if (!alCapabilities.OpenAL10)
            assert false : "Audio library not supported!";
    }

    private static void cleanUpOpenAL()
    {
        alcDestroyContext(audioContext);
        alcCloseDevice(audioDevice);
    }
}