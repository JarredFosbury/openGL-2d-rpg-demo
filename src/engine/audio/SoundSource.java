package engine.audio;

import engine.core.Entity;
import engine.core.EntityType;
import org.joml.Vector3f;

import static org.lwjgl.openal.AL10.*;

public class SoundSource extends Entity
{
    public SoundClip clip;

    private boolean isPlaying;
    private int sourceId;
    private float volume;
    private float pitch;

    public SoundSource(String name, SoundClip clip, boolean loops)
    {
        super(name, EntityType.SoundSource);
        this.clip = clip;
        isPlaying = false;
        volume = 0.5f;
        pitch = 1.0f;
        position = new Vector3f(0.0f);

        sourceId = alGenSources();
        alSourcei(sourceId, AL_BUFFER, clip.getBufferId());
        alSourcei(sourceId, AL_LOOPING, loops ? 1 : 0);
        alSourcei(sourceId, AL_POSITION, 0);
        alSourcef(sourceId, AL_GAIN, volume);
        alSourcef(sourceId, AL_PITCH, pitch);
        alSource3f(sourceId, AL_POSITION, position.x, position.y, position.z);
    }

    public void update()
    {
        alSource3f(sourceId, AL_POSITION, position.x, position.y, position.z);
    }

    public void delete()
    {
        alDeleteSources(sourceId);
    }

    public void play()
    {
        isPlaying = false;
        alSourcei(sourceId, AL_POSITION, 0);

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
}