package engine.core;

import engine.audio.SoundClip;
import engine.fontRendering.Font;
import engine.rendering.Texture;
import engine.shaders.Shader;

import java.util.HashMap;

public class AssetPool
{
    public HashMap<String, Shader> shaderPool;
    public HashMap<String, Texture> texturePool;
    public HashMap<String, SoundClip> soundClipPool;
    public HashMap<String, Font> fontPool;

    private boolean isEmpty;

    public AssetPool()
    {
        shaderPool = new HashMap<>();
        texturePool = new HashMap<>();
        soundClipPool = new HashMap<>();
        fontPool = new HashMap<>();
        isEmpty = true;
    }

    public void addAssetToPool(Object asset, String name)
    {
        if (isEmpty)
            isEmpty = false;

        if (asset.getClass().equals(Shader.class))
            shaderPool.put(name, (Shader) asset);

        if (asset.getClass().equals(Texture.class))
            texturePool.put(name, (Texture) asset);

        if (asset.getClass().equals(SoundClip.class))
            soundClipPool.put(name, (SoundClip) asset);

        if (asset.getClass().equals(Font.class))
            fontPool.put(name, (Font) asset);
    }

    public Object getAssetFromPool(String name)
    {
        Object assetOut = null;

        Shader shaderOut = shaderPool.get(name);
        Texture textureOut = texturePool.get(name);
        SoundClip soundClipOut = soundClipPool.get(name);
        Font fontOut = fontPool.get(name);

        if (shaderOut != null)
            assetOut = shaderOut;

        if (textureOut != null)
            assetOut = textureOut;

        if (soundClipOut != null)
            assetOut = soundClipOut;

        if (fontOut != null)
            assetOut = fontOut;

        return assetOut;
    }

    public void releaseAllAssetsFromPool()
    {
        if (isEmpty)
        {
            System.err.println("ERROR: RELEASE CALLED ON EMPTY POOL\nCannot release assets from an empty asset pool. Nothing to release.");
            return;
        }

        for (String key : shaderPool.keySet())
            shaderPool.get(key).delete();

        for (String key : texturePool.keySet())
            texturePool.get(key).delete();

        for (String key : soundClipPool.keySet())
            soundClipPool.get(key).delete();

        for (String key : fontPool.keySet())
            fontPool.get(key).delete();

        shaderPool.clear();
        texturePool.clear();
        soundClipPool.clear();
        fontPool.clear();
        isEmpty = true;
    }

    public int getTotalPoolSize()
    {
        return shaderPool.size() + texturePool.size() + soundClipPool.size() + fontPool.size();
    }

    public String[] getAllAssetKeys()
    {
        String[] outAssetKeys = new String[getTotalPoolSize()];
        int index = 0;

        for (String key : shaderPool.keySet())
        {
            outAssetKeys[index] = key + ", Shader";
            index ++;
        }

        for (String key : texturePool.keySet())
        {
            outAssetKeys[index] = key + ", Texture";
            index ++;
        }

        for (String key : soundClipPool.keySet())
        {
            outAssetKeys[index] = key + ", SoundClip";
            index ++;
        }

        for (String key : fontPool.keySet())
        {
            outAssetKeys[index] = key + ", Font";
            index ++;
        }

        return outAssetKeys;
    }
}