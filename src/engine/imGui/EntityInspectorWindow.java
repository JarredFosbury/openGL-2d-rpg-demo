package engine.imGui;

import engine.core.Entity;
import imgui.ImGui;
import imgui.flag.ImGuiTreeNodeFlags;
import org.joml.Vector3f;

public class EntityInspectorWindow extends ImGuiWindow
{
    private Entity selectedEntity;

    public EntityInspectorWindow()
    {
        super("entity_inspector_window", "Entity Inspector");
    }

    public void renderWindowContents()
    {
        if (selectedEntity == null)
        {
            ImGui.text("Nothing selected");
            return;
        }

        if (ImGui.collapsingHeader("Identity", ImGuiTreeNodeFlags.DefaultOpen))
        {
            ImGui.labelText("Name", selectedEntity.name);
            ImGui.labelText("Unique Identifier", selectedEntity.ID.toString());
        }

        if (ImGui.collapsingHeader("Transform", ImGuiTreeNodeFlags.DefaultOpen))
        {
            float[] axis = new float[] {selectedEntity.position.x, selectedEntity.position.y, selectedEntity.position.z};
            ImGui.inputFloat3("Position", axis);
            selectedEntity.position = new Vector3f(axis[0], axis[1], axis[2]);

            axis = new float[] {
                    (float) Math.toDegrees(selectedEntity.rotation.x),
                    (float) Math.toDegrees(selectedEntity.rotation.y),
                    (float) Math.toDegrees(selectedEntity.rotation.z)
            };
            ImGui.inputFloat3("Rotation", axis);
            selectedEntity.rotation = new Vector3f((float) Math.toRadians(axis[0]), (float)Math.toRadians(axis[1]), (float)Math.toRadians(axis[2]));

            axis = new float[] {selectedEntity.scale.x, selectedEntity.scale.y, selectedEntity.scale.z};
            ImGui.inputFloat3("Scale", axis);
            selectedEntity.scale = new Vector3f(axis[0], axis[1], axis[2]);
        }
    }

    public void setSelectedEntity(Entity target)
    {
        selectedEntity = target;
    }
}