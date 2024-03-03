package engine.core;

import engine.audio.SoundSource;
import engine.rendering.ScreenSpaceSprite;
import engine.rendering.Sprite;
import engine.rendering.TextMesh;

public enum EntityType
{
    NULL(null),
    Camera(Camera.class),
    Sprite(Sprite.class),
    ScreenSpaceSprite(ScreenSpaceSprite.class),
    TextMesh(TextMesh.class),
    SoundSource(SoundSource.class);

    final Class classType;

    EntityType(Class classType)
    {
        this.classType = classType;
    }
}