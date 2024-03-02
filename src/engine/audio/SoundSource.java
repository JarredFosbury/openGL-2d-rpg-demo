package engine.audio;

import org.joml.Vector3f;

import java.nio.IntBuffer;
import java.nio.ShortBuffer;

import static org.lwjgl.openal.AL10.*;
import static org.lwjgl.stb.STBVorbis.stb_vorbis_decode_filename;
import static org.lwjgl.system.MemoryStack.*;
import static org.lwjgl.system.libc.LibCStdlib.free;

public class SoundSource
{
    public String filepath;

    private boolean isPlaying;
    private int bufferId;
    private int sourceId;
    private float volume;
    private float pitch;
    private Vector3f position;

    public SoundSource(String filepath, boolean loops)
    {
        this.filepath = filepath;
        isPlaying = false;
        volume = 0.5f;
        pitch = 1.0f;
        position = new Vector3f(0.0f);

        stackPush();
        IntBuffer channelsBuffer = stackMallocInt(1);
        stackPush();
        IntBuffer sampleRateBuffer = stackMallocInt(1);

        ShortBuffer rawAudioBuffer = stb_vorbis_decode_filename(filepath, channelsBuffer, sampleRateBuffer);
        if (rawAudioBuffer == null)
        {
            System.err.println("Could not load sound from filepath: " + filepath);
            stackPop();
            stackPop();
            return;
        }

        int channels = channelsBuffer.get();
        int sampleRate = sampleRateBuffer.get();
        stackPop();
        stackPop();

        int format = -1;
        if (channels == 1)
            format = AL_FORMAT_MONO16;
        else if (channels == 2)
            format = AL_FORMAT_STEREO16;

        bufferId = alGenBuffers();
        alBufferData(bufferId, format, rawAudioBuffer, sampleRate);

        sourceId = alGenSources();
        alSourcei(sourceId, AL_BUFFER, bufferId);
        alSourcei(sourceId, AL_LOOPING, loops ? 1 : 0);
        alSourcei(sourceId, AL_POSITION, 0);
        alSourcef(sourceId, AL_GAIN, volume);
        alSourcef(sourceId, AL_PITCH, pitch);
        alSource3f(sourceId, AL_POSITION, position.x, position.y, position.z);

        free(rawAudioBuffer);
    }

    public void delete()
    {
        alDeleteSources(sourceId);
        alDeleteBuffers(bufferId);
    }

    public void play()
    {
        int state = alGetSourcei(sourceId, AL_SOURCE_STATE);
        if (state == AL_STOPPED)
        {
            isPlaying = false;
            alSourcei(sourceId, AL_POSITION, 0);
        }

        if (!isPlaying)
        {
            alSourcePlay(sourceId);
            isPlaying = true;
        }
    }

    public void stop()
    {
        if (isPlaying)
        {
            alSourceStop(sourceId);
            isPlaying = false;
        }
    }

    public boolean isPlaying()
    {
        int state = alGetSourcei(sourceId, AL_SOURCE_STATE);
        if (state == AL_STOPPED)
            isPlaying = false;
        return isPlaying;
    }

    public void setVolume(float volume)
    {
        this.volume = volume;
        alSourcef(sourceId, AL_GAIN, volume);
    }

    public float getVolume()
    {
        return volume;
    }

    public void setPitch(float pitch)
    {
        this.pitch = pitch;
        alSourcef(sourceId, AL_PITCH, pitch);
    }

    public float getPitch()
    {
        return pitch;
    }

    public void translate(float x, float y, float z)
    {
        position.add(x, y, z);
        alSource3f(sourceId, AL_POSITION, position.x, position.y, position.z);
    }

    public void setPosition(float x, float y, float z)
    {
        position = new Vector3f(x, y, z);
        alSource3f(sourceId, AL_POSITION, position.x, position.y, position.z);
    }

    public Vector3f getPosition()
    {
        return position;
    }
}