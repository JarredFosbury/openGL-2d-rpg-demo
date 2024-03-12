package engine.core;

import engine.audio.SoundSource;
import engine.rendering.*;

public enum EntityType
{
    NULL(null),
    ScriptableBehavior(null),
    Camera(Camera.class),
    Sprite(Sprite.class),
    ScreenSpaceSprite(ScreenSpaceSprite.class),
    TextMesh(TextMesh.class),
    SoundSource(SoundSource.class),
    SpriteLit(SpriteLit.class),
    MainLightSource(MainLightSource.class);

    final Class classType;

    EntityType(Class classType)
    {
        this.classType = classType;
    }
}