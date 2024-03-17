package engine.imGui;

import engine.audio.SoundSource;
import engine.core.Camera;
import engine.core.Entity;
import engine.core.KeyListener;
import engine.core.Utils;
import engine.rendering.*;
import imgui.ImGui;
import imgui.flag.ImGuiTreeNodeFlags;
import imgui.type.*;
import org.joml.Vector2f;
import org.joml.Vector2i;
import org.joml.Vector3f;
import org.joml.Vector4f;

import static org.lwjgl.glfw.GLFW.*;

public class EntityInspectorWindow extends ImGuiWindow
{
    private Entity selectedEntity;

    public EntityInspectorWindow()
    {
        super("entity_inspector_window", "Entity Inspector");
    }

    public void hotkeyControls()
    {
        if (KeyListener.isKeyActive(GLFW_KEY_LEFT_ALT) && KeyListener.isKeyPressed(GLFW_KEY_A))
            selectedEntity = null;
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

            ImBoolean visible = new ImBoolean(selectedEntity.isVisible);
            ImGui.checkbox("Visible?", visible);
            selectedEntity.isVisible = visible.get();

            ImInt index = new ImInt(selectedEntity.HIERARCHY_INDEX);
            ImGui.labelText("Hierarchy Index", String.valueOf(index));
        }

        parseEntityType();
    }

    public void setSelectedEntity(Entity target)
    {
        selectedEntity = target;
    }

    private void parseEntityType()
    {
        switch (selectedEntity.TYPE)
        {
            case NULL -> {}
            case ScriptableBehavior -> renderScriptableBehaviorInspector();
            case Camera -> renderCameraInspector();
            case Sprite -> renderSpriteInspector();
            case ScreenSpaceSprite -> renderScreenSpaceSpriteInspector();
            case TextMesh -> renderTextMeshInspector();
            case SoundSource -> renderSoundSourceInspector();
            case SpriteLit -> renderSpriteLitInspector();
            case MainLightSource -> renderMainLightSourceInspector();
            case ScreenSpaceSprite9Slice -> renderScreenSpaceSprite9SliceInspector();
            case PointLightSource -> renderPointLightSourceInspector();
        }
    }

    private void renderScriptableBehaviorInspector()
    {
        if (ImGui.collapsingHeader("Scriptable Behavior", ImGuiTreeNodeFlags.DefaultOpen))
        {
            ImGui.text("Nothing to display at the moment :3");
        }
    }

    private void renderCameraInspector()
    {
        if (ImGui.collapsingHeader("Camera", ImGuiTreeNodeFlags.DefaultOpen))
        {
            Camera camRef = (Camera) selectedEntity;
            ImFloat viewPlane = new ImFloat(camRef.viewPlaneScale);
            ImFloat nearPlane = new ImFloat(camRef.nearPlane);
            ImFloat farPlane = new ImFloat(camRef.farPlane);

            ImGui.inputFloat("View Plane Scale", viewPlane);
            ImGui.inputFloat("Near Plane", nearPlane);
            ImGui.inputFloat("Far Plane", farPlane);

            camRef.updateViewport(viewPlane.get(), nearPlane.get(), farPlane.get());
        }
    }

    private void renderSpriteInspector()
    {
        if (ImGui.collapsingHeader("Sprite Unlit", ImGuiTreeNodeFlags.DefaultOpen))
        {
            Sprite spriteRef = (Sprite) selectedEntity;
            // TODO: allow texture to be changed from inspector using asset viewer
            ImGui.image(spriteRef.mainTexture.getTextureID(), 128.0f, 128.0f);
            ImGui.sameLine();
            ImGui.text("mainTexture");

            Vector4f tint = spriteRef.mainTextureTint;
            float[] col4 = {tint.x, tint.y, tint.z, tint.w};
            ImGui.colorEdit4("mainTexture Tint", col4);
            spriteRef.mainTextureTint = new Vector4f(col4[0], col4[1], col4[2], col4[3]);

            Vector2f offset = spriteRef.mainTextureOffset;
            float[] axis = {offset.x, offset.y};
            ImGui.inputFloat2("UV Offset", axis);
            spriteRef.mainTextureOffset = new Vector2f(axis[0], axis[1]);

            Vector2f scale = spriteRef.mainTextureScale;
            float[] factor = {scale.x, scale.y};
            ImGui.inputFloat2("UV Scale", factor);
            spriteRef.mainTextureScale = new Vector2f(factor[0], factor[1]);

            ImInt frameRate = new ImInt(spriteRef.animationFrameRate);
            ImGui.inputInt("Animation Frame Rate", frameRate);
            spriteRef.animationFrameRate = frameRate.get();

            ImGui.labelText("Current Frame", String.valueOf(spriteRef.spriteSheetFrame));

            if (ImGui.button("Render Next Sprite Frame"))
                spriteRef.nextSpriteSheetFrame();
        }
    }

    private void renderScreenSpaceSpriteInspector()
    {
        if (ImGui.collapsingHeader("Screen Space Sprite", ImGuiTreeNodeFlags.DefaultOpen))
        {
            ScreenSpaceSprite spriteRef = (ScreenSpaceSprite) selectedEntity;
            // TODO: allow texture to be changed from inspector using asset viewer
            ImGui.image(spriteRef.mainTexture.getTextureID(), 128.0f, 128.0f);
            ImGui.sameLine();
            ImGui.text("mainTexture");

            Vector4f tint = spriteRef.mainTextureTint;
            float[] col4 = {tint.x, tint.y, tint.z, tint.w};
            ImGui.colorEdit4("mainTexture Tint", col4);
            spriteRef.mainTextureTint = new Vector4f(col4[0], col4[1], col4[2], col4[3]);

            ImBoolean isAnchored = new ImBoolean(spriteRef.isLocationAnchored);
            ImGui.checkbox("Use Location Anchor", isAnchored);
            spriteRef.isLocationAnchored = isAnchored.get();

            int[] anchorAxis = {spriteRef.locationAnchor.x, spriteRef.locationAnchor.y};
            ImGui.inputInt2("Location Anchor", anchorAxis);
            spriteRef.locationAnchor = new Vector2i(Utils.clampInt(anchorAxis[0], -1, 1), Utils.clampInt(anchorAxis[1], -1, 1));
        }
    }

    private void renderTextMeshInspector()
    {
        if (ImGui.collapsingHeader("Text Mesh", ImGuiTreeNodeFlags.DefaultOpen))
        {
            TextMesh meshRef = (TextMesh) selectedEntity;
            ImGui.labelText("Font Name", meshRef.font.name);
            ImGui.labelText("Glyph Count", String.valueOf(meshRef.font.glyphs.length));
            // TODO: allow font to be changed from inspector using asset viewer
            ImGui.image(meshRef.font.bitmap.getTextureID(), 256, 256);
            ImGui.sameLine();
            ImGui.text("Font Atlas Texture");

            ImBoolean isAnchored = new ImBoolean(meshRef.isLocationAnchored);
            ImGui.checkbox("Use Location Anchor", isAnchored);
            meshRef.isLocationAnchored = isAnchored.get();

            int[] anchorAxis = {meshRef.locationAnchor.x, meshRef.locationAnchor.y};
            ImGui.inputInt2("Location Anchor", anchorAxis);
            meshRef.locationAnchor = new Vector2i(Utils.clampInt(anchorAxis[0], -1, 1), Utils.clampInt(anchorAxis[1], -1, 1));

            int[] alignmentAxis = {meshRef.textAlignment.x, meshRef.textAlignment.y};
            ImGui.inputInt2("Text Alignment", alignmentAxis);
            meshRef.textAlignment = new Vector2i(Utils.clampInt(alignmentAxis[0], -1, 1), Utils.clampInt(alignmentAxis[1], -1, 1));

            Vector4f tint = meshRef.colorRGBA;
            float[] col4 = {tint.x, tint.y, tint.z, tint.w};
            ImGui.colorEdit4("Text Color", col4);
            meshRef.colorRGBA = new Vector4f(col4[0], col4[1], col4[2], col4[3]);

            ImFloat fontSize = new ImFloat(meshRef.fontSize_PIXELS);
            ImGui.inputFloat("Font Size", fontSize);
            meshRef.fontSize_PIXELS = fontSize.get();

            ImString text = new ImString(meshRef.text, 256);
            ImGui.inputText("Text", text);
            meshRef.text = text.get();

            float[] bounds = {meshRef.boundSize.x, meshRef.boundSize.y};
            ImGui.inputFloat2("Mesh Bounds", bounds);

            int indexLength = 0;
            int vertexLength = 0;
            int triangles = 0;

            if (meshRef.indexData != null && meshRef.vertexData != null)
            {
                indexLength = meshRef.indexData.length;
                vertexLength = meshRef.vertexData.length;
                triangles = meshRef.vertexData.length/3;
            }

            ImGui.labelText("Mesh Index Count", String.valueOf(indexLength));
            ImGui.labelText("Mesh Vertex Count", String.valueOf(vertexLength));
            ImGui.labelText("Mesh Triangle Count", String.valueOf(triangles));
        }
    }

    private void renderSoundSourceInspector()
    {
        if (ImGui.collapsingHeader("Sound Source", ImGuiTreeNodeFlags.DefaultOpen))
        {
            SoundSource sourceRef = (SoundSource) selectedEntity;
            ImGui.labelText("Sound Clip Buffer ID", String.valueOf(sourceRef.clip.getBufferId()));

            ImFloat volume = new ImFloat(sourceRef.volume);
            ImGui.inputFloat("Volume", volume);
            sourceRef.volume = volume.get();

            ImFloat pitch = new ImFloat(sourceRef.pitch);
            ImGui.inputFloat("Pitch", pitch);
            sourceRef.pitch = pitch.get();

            ImFloat falloffRange = new ImFloat(sourceRef.falloffRange);
            ImGui.inputFloat("Fall-off Range", falloffRange);
            sourceRef.falloffRange = falloffRange.get();
        }
    }

    private void renderSpriteLitInspector()
    {
        if (ImGui.collapsingHeader("Sprite Normal Mapped and Lit", ImGuiTreeNodeFlags.DefaultOpen))
        {
            SpriteLit spriteRef = (SpriteLit) selectedEntity;
            // TODO: allow textures to be changed from inspector using asset viewer
            ImGui.image(spriteRef.mainTexture.getTextureID(), 128.0f, 128.0f);
            ImGui.sameLine();
            ImGui.text("mainTexture");

            ImGui.image(spriteRef.normalTexture.getTextureID(), 128.0f, 128.0f);
            ImGui.sameLine();
            ImGui.text("normalTexture");

            Vector4f tint = spriteRef.mainTextureTint;
            float[] col4 = {tint.x, tint.y, tint.z, tint.w};
            ImGui.colorEdit4("mainTexture Tint", col4);
            spriteRef.mainTextureTint = new Vector4f(col4[0], col4[1], col4[2], col4[3]);

            Vector2f offset = spriteRef.mainTextureOffset;
            float[] axis = {offset.x, offset.y};
            ImGui.inputFloat2("UV Offset", axis);
            spriteRef.mainTextureOffset = new Vector2f(axis[0], axis[1]);

            Vector2f scale = spriteRef.mainTextureScale;
            float[] factor = {scale.x, scale.y};
            ImGui.inputFloat2("UV Scale", factor);
            spriteRef.mainTextureScale = new Vector2f(factor[0], factor[1]);

            ImInt frameRate = new ImInt(spriteRef.animationFrameRate);
            ImGui.inputInt("Animation Frame Rate", frameRate);
            spriteRef.animationFrameRate = frameRate.get();

            ImGui.labelText("Current Frame", String.valueOf(spriteRef.spriteSheetFrame));

            if (ImGui.button("Render Next Sprite Frame"))
                spriteRef.nextSpriteSheetFrame();
        }
    }

    private void renderMainLightSourceInspector()
    {
        if (ImGui.collapsingHeader("Main Directional Light Source", ImGuiTreeNodeFlags.DefaultOpen))
        {
            MainLightSource sourceRef = (MainLightSource) selectedEntity;
            Vector4f tint = sourceRef.color;
            float[] col4 = {tint.x, tint.y, tint.z, tint.w};
            ImGui.colorEdit4("Light Color", col4);
            sourceRef.color = new Vector4f(col4[0], col4[1], col4[2], col4[3]);

            ImFloat intensity = new ImFloat(sourceRef.intensity);
            ImGui.inputFloat("Intensity", intensity);
            sourceRef.intensity = intensity.get();
        }
    }

    private void renderScreenSpaceSprite9SliceInspector()
    {
        if (ImGui.collapsingHeader("9-slice Screen Space Sprite", ImGuiTreeNodeFlags.DefaultOpen))
        {
            ScreenSpace9SliceSprite spriteRef = (ScreenSpace9SliceSprite) selectedEntity;
            // TODO: allow texture to be changed from inspector using asset viewer
            ImGui.image(spriteRef.mainTexture.getTextureID(), 128.0f, 128.0f);
            ImGui.sameLine();
            ImGui.text("mainTexture");

            Vector4f tint = spriteRef.mainTextureTint;
            float[] col4 = {tint.x, tint.y, tint.z, tint.w};
            ImGui.colorEdit4("mainTexture Tint", col4);
            spriteRef.mainTextureTint = new Vector4f(col4[0], col4[1], col4[2], col4[3]);

            ImBoolean isAnchored = new ImBoolean(spriteRef.isLocationAnchored);
            ImGui.checkbox("Use Location Anchor", isAnchored);
            spriteRef.isLocationAnchored = isAnchored.get();

            int[] anchorAxis = {spriteRef.locationAnchor.x, spriteRef.locationAnchor.y};
            ImGui.inputInt2("Location Anchor", anchorAxis);
            spriteRef.locationAnchor = new Vector2i(Utils.clampInt(anchorAxis[0], -1, 1), Utils.clampInt(anchorAxis[1], -1, 1));

            Vector4f sliceBorders = spriteRef.sliceBorders;
            float[] borders = {sliceBorders.x, sliceBorders.y, sliceBorders.z, sliceBorders.w};
            ImGui.inputFloat4("Slice Borders", borders);
            spriteRef.sliceBorders = new Vector4f(borders[0], borders[1], borders[2], borders[3]);
        }
    }

    private void renderPointLightSourceInspector()
    {
        if (ImGui.collapsingHeader("Point Light Source", ImGuiTreeNodeFlags.DefaultOpen))
        {
            PointLightSource sourceRef = (PointLightSource) selectedEntity;
            Vector4f tint = sourceRef.color;
            float[] col4 = {tint.x, tint.y, tint.z, tint.w};
            ImGui.colorEdit4("Light Color", col4);
            sourceRef.color = new Vector4f(col4[0], col4[1], col4[2], col4[3]);

            ImFloat intensity = new ImFloat(sourceRef.intensity);
            ImGui.inputFloat("Intensity", intensity);
            sourceRef.intensity = intensity.get();

            ImFloat range = new ImFloat(sourceRef.range);
            ImGui.inputFloat("Range", range);
            sourceRef.range = range.get();

            ImGui.text("Falloff Factors");
            ImFloat constant = new ImFloat(sourceRef.constant);
            ImGui.inputFloat("Constant", constant);
            sourceRef.constant = constant.get();

            ImFloat linear = new ImFloat(sourceRef.linear);
            ImGui.inputFloat("Linear", linear);
            sourceRef.linear = linear.get();

            ImFloat exponent = new ImFloat(sourceRef.exponent);
            ImGui.inputFloat("Exponent", exponent);
            sourceRef.exponent = exponent.get();
        }
    }
}