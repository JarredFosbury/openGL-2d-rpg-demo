package engine.imGui;

import engine.core.Entity;
import engine.core.Scene;
import engine.patternInterfaces.Observable;
import engine.patternInterfaces.Observer;
import imgui.ImGui;

public class SceneEntityViewer extends ImGuiWindow implements Observer
{
    private Entity[] entityList;

    public SceneEntityViewer()
    {
        super("scene_entity_list_window", "Scene Entity Viewer (0 entities)");
        Scene.entities.addObserver(this);
        entityList = new Entity[0];
    }

    public void renderWindowContents()
    {
        setTitle("Scene Entity List (" + entityList.length + " entities)");
        if (entityList.length > 0)
            for (int i = 0; i < entityList.length; i++)
                if (ImGui.selectable(entityList[i].name + "###" + entityList[i].ID))
                    ((EntityInspectorWindow) Scene.findImGuiComponents("entity_inspector_window")[0]).setSelectedEntity(entityList[i]);
    }

    @Override
    public void update(Observable target)
    {
        entityList = new Entity[Scene.entities.size()];
        for (int i = 0; i < entityList.length; i++)
            entityList[i] = Scene.entities.get(i);
    }
}