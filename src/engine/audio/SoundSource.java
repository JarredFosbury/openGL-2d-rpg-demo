package engine.audio;

import engine.core.Entity;
import engine.core.EntityType;
import engine.core.Scene;
import org.joml.Vector3f;

import static org.lwjgl.openal.AL10.*;

public class SoundSource extends Entity
{
    public SoundClip clip;
    public float volume;
    public float pitch;
    public float falloffRange;

    private final Listener audioListener;
    private boolean isPlaying;
    private int sourceId;

    public SoundSource(String name, int HIERARCHY_INDEX, SoundClip clip, boolean loops)
    {
        super(name, EntityType.SoundSource, HIERARCHY_INDEX);
        this.clip = clip;
        isPlaying = false;
        volume = 0.5f;
        pitch = 1.0f;
        falloffRange = 25.0f;
        audioListener = Scene.audioListener;

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
        alSourcef(sourceId, AL_GAIN, volume * calculateFalloffCoefficient());
        alSourcef(sourceId, AL_PITCH, pitch);
    }

    public void delete()
    {
        alDeleteSources(sourceId);
    }

    public void play()
    {
        stop();
        alSourcei(sourceId, AL_BUFFER, clip.getBufferId());
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

    public float calculateFalloffCoefficient()
    {
        float dist = Vector3f.distance(position.x, position.y, position.z, audioListener.position.x, audioListener.position.y, audioListener.position.z);
        return Math.max(1.0f - (dist / falloffRange), 0.0f);
    }

    public void cleanUp()
    {
        stop();
    }
}