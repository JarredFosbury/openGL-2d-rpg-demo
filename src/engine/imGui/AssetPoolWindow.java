package engine.imGui;

import engine.core.Scene;
import engine.patternInterfaces.Observable;
import engine.patternInterfaces.Observer;

public class AssetPoolWindow extends ImGuiWindow implements Observer
{
    public AssetPoolWindow()
    {
        super("asset_pool_window", "Asset Pool Viewer");
        Scene.assets.addObserver(this);
    }

    public void renderWindowContents()
    {}

    @Override
    public void update(Observable target)
    {}
}