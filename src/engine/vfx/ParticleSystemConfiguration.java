package engine.vfx;

import engine.rendering.Color;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;

@SuppressWarnings("All")
public class ParticleSystemConfiguration
{
    public static enum Shape
    {
        Point,
        Box,
        Circle;
    }

    public class EmissionModule
    {
        public boolean use = false;
        public float emitsPerSecond = 0.0f;
        public float emitsPerMeter = 0.0f;
    }

    public class VelocityOverLifetimeModule
    {
        public boolean use = false;
        public boolean useRandomizedLinear = false;
        public Vector3f linearFixed = new Vector3f(0.0f);
        public Vector3f linearRandomMin = new Vector3f(-1.0f);
        public Vector3f linearRandomMax = new Vector3f(1.0f);
    }

    public class ShapeModule
    {
        public boolean use = false;
        public Shape shape = Shape.Point;
        public float width = 1.0f;
        public float height = 1.0f;
        public float radius = 0.5f;
        public boolean spawnOnEdgeOnly = false;
        public float edgeThickness = 0.1f;
    }

    public int maxParticleCount = 100;
    public float systemDuration = 5.0f;
    public boolean looping = false;
    public float startDelay = 0.0f;
    public float particleLifetime = 5.0f;
    public float speed = 2.0f;
    public Vector3f size = new Vector3f(1.0f);
    public float rotation = 0.0f;
    public boolean startWithRandomizedRotation = false;
    public float randomRotationMin = -180.0f;
    public float randomRotationMax = 180.0f;
    public Vector4f color = Color.WHITE;
    public float gravity = 0.0f;
    public boolean playOnStart = true;
    public String textureKey = "";
    public boolean useSpriteSheetAnimation = false;
    public String spriteSheetFilepath = "";
    public Vector2f textureOffset = new Vector2f(0.0f);
    public Vector2f textureScale = new Vector2f(1.0f);

    public EmissionModule emissionModule;
    public VelocityOverLifetimeModule velocityOverLifetimeModule;
    public ShapeModule shapeModule;

    public ParticleSystemConfiguration()
    {
        emissionModule = new EmissionModule();
        velocityOverLifetimeModule = new VelocityOverLifetimeModule();
        shapeModule = new ShapeModule();
    }
}