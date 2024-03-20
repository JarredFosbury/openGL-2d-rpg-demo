package game;

import engine.audio.SoundSource;
import engine.core.*;
import engine.rendering.Color;
import engine.rendering.PointLightSource;
import engine.rendering.Sprite;
import engine.rendering.SpriteLit;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;

public class WallTorch extends Entity
{
    private final SpriteLit torchSprite;
    private final Sprite flameAnimatedSprite;
    private final PointLightSource source;
    private final SoundSource audioSource;

    private final float[] randomizedIntensities;
    private final float minIntensity;
    private final float maxIntensity;
    private final float timeBetweenIntensityChange_SECONDS;

    private float intensityChangeTimer;
    private short intensityIndex;

    public WallTorch(String name, int HIERARCHY_INDEX)
    {
        super(name, EntityType.ScriptableBehavior, HIERARCHY_INDEX);
        torchSprite = new SpriteLit(name + "_torchSprite", HIERARCHY_INDEX, "torchAlb",
                "torchNrm", Color.WHITE, new Vector2f(0.0f), new Vector2f(1.0f));
        torchSprite.scale = new Vector3f(0.5f);

        flameAnimatedSprite = new Sprite(name + "_flameAnimatedSprite", HIERARCHY_INDEX + 1,
                "fireSheet", Color.WHITE, new Vector2f(0.0f, 0.75f), new Vector2f(0.125f, 0.25f));
        flameAnimatedSprite.initSpriteSheet("res/spriteSheetData/fireSheetData.ssd");
        flameAnimatedSprite.scale = new Vector3f(0.5f, 1.0f, 1.0f).mul(0.3f);

        source = new PointLightSource(name + "_pointSource", HIERARCHY_INDEX, new Vector4f(0.941f, 0.616f, 0.027f, 1.0f),
                3.0f, 50.0f, 0.0f, 0.0f, 2.0f);
        audioSource = new SoundSource(name + "_soundSource", HIERARCHY_INDEX, Scene.assets.soundClipPool.get("fireSmall"), true);

        minIntensity = 2.0f;
        maxIntensity = 3.0f;
        timeBetweenIntensityChange_SECONDS = 0.25f;

        randomizedIntensities = new float[50];
        for (int i = 0; i < randomizedIntensities.length; i++)
            randomizedIntensities[i] = Utils.randomRangeFloat(minIntensity, maxIntensity);

        intensityChangeTimer = timeBetweenIntensityChange_SECONDS;
        intensityIndex = 0;
        audioSource.play();
    }

    public void update()
    {
        torchSprite.position = position;
        source.position = new Vector3f(position.x + 0.0f, position.y + 0.25f, position.z + 1.0f);
        flameAnimatedSprite.position = new Vector3f(position.x + 0.0f, position.y + 0.25f, position.z + 1.0f);
        flameAnimatedSprite.animateSprite();

        intensityChangeTimer -= Time.deltaTime;
        if (intensityChangeTimer <= 0.0f)
        {
            intensityChangeTimer = timeBetweenIntensityChange_SECONDS;
            intensityIndex ++;
            if (intensityIndex == randomizedIntensities.length)
                intensityIndex = 0;
        }

        if (intensityIndex > 0)
        {
            source.intensity = Utils.lerp(randomizedIntensities[intensityIndex - 1], randomizedIntensities[intensityIndex],
                    1.0f - (intensityChangeTimer / timeBetweenIntensityChange_SECONDS));
        }
        else
        {
            source.intensity = Utils.lerp(randomizedIntensities[randomizedIntensities.length - 1], randomizedIntensities[intensityIndex],
                    1.0f - (intensityChangeTimer / timeBetweenIntensityChange_SECONDS));
        }
    }
}
