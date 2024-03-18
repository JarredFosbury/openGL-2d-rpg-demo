package game;

import engine.core.Entity;
import engine.core.EntityType;
import engine.rendering.Color;
import org.joml.Vector2i;
import org.joml.Vector3f;
import org.joml.Vector4f;

public class PlayerGameHUD extends Entity
{
    public final HUDBar healthBar;
    public final HUDBar staminaBar;
    public final HUDBar experienceBar;
    public final HUDItemSlot weaponItemSlot;
    public final HUDItemSlot consumableItemSlot;
    public final HUDItemSlot flaskItemSlot;

    public PlayerGameHUD(int HIERARCHY_INDEX)
    {
        super("playerGameHUD", EntityType.ScriptableBehavior, HIERARCHY_INDEX);
        healthBar = new HUDBar("healthBar", HIERARCHY_INDEX, true,
                new Vector4f(0.675f, 0.0f, 0.0f, 1.0f), new Vector4f(0.8f, 0.631f, 0.0f, 1.0f), 300.0f, 15.0f);
        healthBar.locationAnchor = new Vector2i(-1, 1);
        healthBar.position = new Vector3f(165.0f, -64.0f, 0.0f);

        staminaBar = new HUDBar("staminaBar", HIERARCHY_INDEX, false, new Vector4f(0.196f, 0.584f, 0.118f, 1.0f),
                Color.WHITE, 300.0f, 15.0f);
        staminaBar.locationAnchor = new Vector2i(-1, 1);
        staminaBar.position = new Vector3f(165.0f, -42.0f, 0.0f);

        experienceBar = new HUDBar("experienceBar", HIERARCHY_INDEX, false, new Vector4f(0.431f, 0.0f, 0.639f, 1.0f),
                Color.WHITE, 300.0f, 15.0f);
        experienceBar.locationAnchor = new Vector2i(-1, 1);
        experienceBar.position = new Vector3f(165.0f, -20.0f, 0.0f);

        weaponItemSlot = new HUDItemSlot("weaponItemSlot", HIERARCHY_INDEX,
                true, new Vector4f(0.718f, 0.537f, 0.314f, 1.0f));
        weaponItemSlot.locationAnchor = new Vector2i(1, 1);
        weaponItemSlot.position = new Vector3f(-60.0f, -90.0f, 0.0f);

        consumableItemSlot = new HUDItemSlot("consumableItemSlot", HIERARCHY_INDEX,
                false, Color.WHITE);
        consumableItemSlot.locationAnchor = new Vector2i(1, 1);
        consumableItemSlot.position = new Vector3f(-160.0f, -90.0f, 0.0f);

        flaskItemSlot = new HUDItemSlot("flaskItemSlot", HIERARCHY_INDEX,
                true, new Vector4f(0.059f, 0.329f, 1.0f, 1.0f));
        flaskItemSlot.locationAnchor = new Vector2i(1, 1);
        flaskItemSlot.position = new Vector3f(-260.0f, -90.0f, 0.0f);
    }


}
