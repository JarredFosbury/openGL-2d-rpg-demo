package engine.imGui;

import engine.core.Scene;
import engine.patternInterfaces.Observable;
import engine.patternInterfaces.Observer;
import imgui.ImGui;

public class AssetPoolWindow extends ImGuiWindow implements Observer
{
    private short assetCount;
    private String[] assets;

    public AssetPoolWindow()
    {
        super("asset_pool_window", "Asset Pool Viewer (0 assets)", true);
        Scene.assets.addObserver(this);
    }

    public void renderWindowContents()
    {
        setTitle("Loaded Asset Pool (" + assetCount + " assets)");

        for (int i = 0; i < assets.length; i++)
        {
            String[] halves = assets[i].split(",");
            ImGui.selectable("(" + halves[0] + ")" + halves[1]);
        }
    }

    @Override
    public void update(Observable target)
    {
        assetCount = Scene.assets.getTotalPoolSize();
        assets = Scene.assets.getAllAssetKeys();
    }
}