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

        if (assets != null)
        {
            for (String asset : assets)
            {
                String[] halves = asset.split(",");
                ImGui.selectable("(" + halves[0] + ")" + halves[1]);
            }
        }
        else
        {
            ImGui.text("No assets loaded");
        }
    }

    @Override
    public void update(Observable target)
    {
        assetCount = Scene.assets.getTotalPoolSize();
        assets = Scene.assets.getAllAssetKeys();
    }
}