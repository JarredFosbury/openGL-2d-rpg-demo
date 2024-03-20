package engine.vfx;

import engine.core.*;
import engine.rendering.SpriteSheetDataLoader;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;

public class ParticleSystem extends Entity
{
    public final ParticleSystemConfiguration CONFIG;

    private final short maxParticleCount;
    private final Particle[] particlePool;

    private short nextParticleIndex;
    private float systemLifetime;
    private boolean isPlaying;
    private float timeToNextEmit;
    private float distanceToNextEmit;

    public ParticleSystem(String name, int HIERARCHY_INDEX, ParticleSystemConfiguration CONFIG)
    {
        super(name, EntityType.ParticleSystem, HIERARCHY_INDEX);
        this.CONFIG = CONFIG;
        this.maxParticleCount = (short) Utils.clampInt(CONFIG.maxParticleCount, Short.MIN_VALUE, Short.MAX_VALUE);
        particlePool = new Particle[maxParticleCount];
        this.nextParticleIndex = 0;
        this.systemLifetime = CONFIG.startDelay * -1.0f;
        this.isPlaying = CONFIG.playOnStart;
        this.timeToNextEmit = 1.0f / CONFIG.emissionModule.emitsPerSecond;
        this.distanceToNextEmit = 1.0f / CONFIG.emissionModule.emitsPerMeter;

        initParticlePool();
    }

    private void initParticlePool()
    {
        Vector2f[] offsets = null;
        if (CONFIG.useSpriteSheetAnimation)
            offsets = SpriteSheetDataLoader.loadSheetDataFromPath(CONFIG.spriteSheetFilepath);

        for (int i = 0; i < maxParticleCount; i++)
        {
            particlePool[i] = new Particle(
                    CONFIG.textureKey,
                    CONFIG.color,
                    CONFIG.textureOffset,
                    CONFIG.textureScale,
                    offsets
            );
            particlePool[i].isActive = false;
            particlePool[i].lifeRemaining = CONFIG.particleLifetime;
            particlePool[i].scale = CONFIG.size;
        }
    }

    public void update()
    {
        if (!isVisible)
            return;

        emissionLogic();
        particleLogic();
    }

    private void emissionLogic()
    {
        if (isPlaying)
        {
            systemLifetime += Time.deltaTime;
            if (systemLifetime >= CONFIG.systemDuration)
            {
                if (CONFIG.looping)
                    systemLifetime = 0.0f;
                else
                    isPlaying = false;
            }

            if (systemLifetime > 0.0f)
            {
                timeToNextEmit -= Time.deltaTime;
                if (timeToNextEmit <= 0.0f)
                {
                    timeToNextEmit = 1.0f / CONFIG.emissionModule.emitsPerSecond;
                    emit();
                }
            }
        }
    }

    private void particleLogic()
    {
        for (int i = 0; i < maxParticleCount; i++)
        {
            if (!particlePool[i].isActive)
                continue;

            if (particlePool[i].lifeRemaining <= 0.0f)
            {
                particlePool[i].isActive = false;
                continue;
            }

            particlePool[i].lifeRemaining -= Time.deltaTime;
            if (CONFIG.useSpriteSheetAnimation)
                particlePool[i].animateParticle();
        }
    }

    public void render()
    {
        if (!isVisible || systemLifetime < 0.0f)
            return;

        Matrix4f transformation = Scene.mainCamera.getTransformation();
        for (int i = 0; i < maxParticleCount; i++)
            particlePool[i].render(transformation);
    }

    public void emit()
    {
        if (CONFIG.shapeModule.use)
        {
            switch (CONFIG.shapeModule.shape)
            {
                case Point -> particlePool[nextParticleIndex].position = position;
                case Box -> {
                    Vector2f vectorPos;
                    float halfWidth = CONFIG.shapeModule.width / 2.0f;
                    float halfHeight = CONFIG.shapeModule.height / 2.0f;
                    if (CONFIG.shapeModule.spawnOnEdgeOnly)
                    {
                        float zoneAEnd, zoneBEnd, zoneCEnd, zoneDEnd;
                        zoneAEnd = CONFIG.shapeModule.height + (CONFIG.shapeModule.edgeThickness * 2.0f);
                        zoneBEnd = zoneAEnd + CONFIG.shapeModule.width;
                        zoneCEnd = zoneBEnd + CONFIG.shapeModule.height + (CONFIG.shapeModule.edgeThickness * 2.0f);
                        zoneDEnd = zoneCEnd + CONFIG.shapeModule.width;
                        Vector2f sampleZone = new Vector2f(zoneDEnd, CONFIG.shapeModule.edgeThickness);
                        Vector2f randomPoint = new Vector2f(Utils.randomRangeFloat(0.0f, sampleZone.x), Utils.randomRangeFloat(0.0f, sampleZone.y));
                        if (randomPoint.x <= zoneAEnd)
                        {
                            float x = (CONFIG.shapeModule.width * -0.5f) - randomPoint.y;
                            float y = randomPoint.x - (zoneAEnd / 2.0f);
                            vectorPos = new Vector2f(x, y);
                        }
                        else if (randomPoint.x <= zoneBEnd)
                        {
                            float x = (randomPoint.x - zoneAEnd) - (zoneBEnd - zoneAEnd) / 2.0f;
                            float y = (CONFIG.shapeModule.height * -0.5f) - randomPoint.y;
                            vectorPos = new Vector2f(x, y);
                        }
                        else if (randomPoint.x <= zoneCEnd)
                        {
                            float x = (CONFIG.shapeModule.width * 0.5f) + randomPoint.y;
                            float y = (randomPoint.x - zoneBEnd) - (zoneCEnd - zoneBEnd) / 2.0f;
                            vectorPos = new Vector2f(x, y);
                        }
                        else if (randomPoint.x <= zoneDEnd)
                        {
                            float x = (randomPoint.x - zoneCEnd) - (zoneDEnd - zoneCEnd) / 2.0f;
                            float y = (CONFIG.shapeModule.height * 0.5f) + randomPoint.y;
                            vectorPos = new Vector2f(x, y);
                        }
                        else
                        {
                            vectorPos = new Vector2f(0.0f);
                        }
                    }
                    else
                    {
                        vectorPos = new Vector2f(Utils.randomRangeFloat(-halfWidth, halfWidth), Utils.randomRangeFloat(-halfHeight, halfHeight));
                    }

                    particlePool[nextParticleIndex].position = new Vector3f(position).add(new Vector3f(vectorPos, 1.0f));
                }
                case Circle -> {
                    float angleTheta = (float) Math.toRadians(Utils.randomRangeFloat(-180.0f, 180.0f));
                    Vector2f vectorTheta = new Vector2f((float) Math.cos(angleTheta), (float) Math.sin(angleTheta));

                    if (CONFIG.shapeModule.spawnOnEdgeOnly)
                    {
                        float min = CONFIG.shapeModule.radius - CONFIG.shapeModule.edgeThickness * 0.5f;
                        float max = CONFIG.shapeModule.radius +  CONFIG.shapeModule.edgeThickness * 0.5f;
                        vectorTheta.mul(Utils.randomRangeFloat(min, max));
                    }
                    else
                    {
                        vectorTheta.mul(Utils.randomRangeFloat(0.0f, CONFIG.shapeModule.radius));
                    }

                    particlePool[nextParticleIndex].position = new Vector3f(position).add(new Vector3f(vectorTheta, 1.0f));
                }
            }
        }
        else
        {
            particlePool[nextParticleIndex].position = position;
        }

        if (!CONFIG.startWithRandomizedRotation)
        {
            particlePool[nextParticleIndex].rotation = new Vector3f(0.0f, 0.0f, CONFIG.rotation);
        }
        else
        {
            particlePool[nextParticleIndex].rotation = new Vector3f(0.0f, 0.0f,
                    Utils.randomRangeFloat(CONFIG.randomRotationMin, CONFIG.randomRotationMax));
        }

        particlePool[nextParticleIndex].scale = new Vector3f(CONFIG.size);
        if (CONFIG.useSpriteSheetAnimation)
            particlePool[nextParticleIndex].spriteSheetFrame = 0;

        particlePool[nextParticleIndex].lifeRemaining = CONFIG.particleLifetime;
        particlePool[nextParticleIndex].isActive = true;

        nextParticleIndex++;
        if (nextParticleIndex == maxParticleCount)
            nextParticleIndex = 0;
    }

    public void play()
    {
        if (isPlaying)
            return;

        systemLifetime = CONFIG.startDelay * -1.0f;
        timeToNextEmit = 1.0f / CONFIG.emissionModule.emitsPerSecond;
        distanceToNextEmit = 1.0f / CONFIG.emissionModule.emitsPerMeter;
        isPlaying = true;
    }

    public void stop()
    {
        if (!isPlaying)
            return;

        systemLifetime = 0.0f;
        isPlaying = false;

        for (int i = 0; i < maxParticleCount; i++)
            particlePool[i].isActive = false;
    }

    public int getNextParticleIndex()
    {
        return nextParticleIndex;
    }

    public float getSystemLifetime()
    {
        return systemLifetime;
    }
}
