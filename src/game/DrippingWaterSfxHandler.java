package game;

import engine.audio.SoundClip;
import engine.audio.SoundSource;
import engine.core.*;

public class DrippingWaterSfxHandler extends Entity
{
    private final SoundSource source;
    private final SoundClip[] clips;
    private float timeTillNextSound;

    public DrippingWaterSfxHandler()
    {
        super("drippingWaterSfxHandler", EntityType.ScriptableBehavior, 0);

        clips = new SoundClip[] {Scene.assets.soundClipPool.get("caveDrip01"), Scene.assets.soundClipPool.get("caveDrip02"),
                Scene.assets.soundClipPool.get("caveDrip03"), Scene.assets.soundClipPool.get("caveDrip04")};
        source = new SoundSource("caveDripSfx-soundSource", 0, clips[0], false);
        source.volume = 1.0f;
    }

    public void update()
    {
        if (timeTillNextSound <= 0.0f)
        {
            timeTillNextSound = Utils.randomRangeFloat(3.0f, 5.0f);
            playRandomSfx();
        }

        timeTillNextSound -= Time.deltaTime;
    }

    private void playRandomSfx()
    {
        source.clip = clips[Utils.randomRangeInteger(0, clips.length - 1)];
        source.play();
    }
}