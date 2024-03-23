package game;

import engine.core.Entity;
import engine.core.EntityType;
import engine.rendering.Color;
import org.joml.Vector2i;
import org.joml.Vector3f;
import org.joml.Vector4f;

public class PlayerGameHUD extends Entity
{
    public final HUDBar HEALTH_BAR;
    public final HUDBar STAMINA_BAR;
    public final HUDBar EXPERIENCE_BAR;
    public final HUDItemSlot WEAPON_ITEM_SLOT;
    public final HUDItemSlot CONSUMABLE_ITEM_SLOT;
    public final HUDItemSlot FLASK_ITEM_SLOT;

    public PlayerGameHUD(int HIERARCHY_INDEX)
    {
        super("playerGameHUD", EntityType.ScriptableBehavior, HIERARCHY_INDEX);
        HEALTH_BAR = new HUDBar("healthBar", HIERARCHY_INDEX, true,
                new Vector4f(0.675f, 0.0f, 0.0f, 1.0f), new Vector4f(0.8f, 0.631f, 0.0f, 1.0f), 300.0f, 15.0f);
        HEALTH_BAR.locationAnchor = new Vector2i(-1, 1);
        HEALTH_BAR.position = new Vector3f(165.0f, -64.0f, 0.0f);

        STAMINA_BAR = new HUDBar("staminaBar", HIERARCHY_INDEX, false, new Vector4f(0.196f, 0.584f, 0.118f, 1.0f),
                Color.WHITE, 300.0f, 15.0f);
        STAMINA_BAR.locationAnchor = new Vector2i(-1, 1);
        STAMINA_BAR.position = new Vector3f(165.0f, -42.0f, 0.0f);

        EXPERIENCE_BAR = new HUDBar("experienceBar", HIERARCHY_INDEX, false, new Vector4f(0.431f, 0.0f, 0.639f, 1.0f),
                Color.WHITE, 300.0f, 15.0f);
        EXPERIENCE_BAR.locationAnchor = new Vector2i(-1, 1);
        EXPERIENCE_BAR.position = new Vector3f(165.0f, -20.0f, 0.0f);

        WEAPON_ITEM_SLOT = new HUDItemSlot("weaponItemSlot", HIERARCHY_INDEX,
                true, new Vector4f(0.718f, 0.537f, 0.314f, 1.0f));
        WEAPON_ITEM_SLOT.locationAnchor = new Vector2i(1, 1);
        WEAPON_ITEM_SLOT.position = new Vector3f(-60.0f, -90.0f, 0.0f);

        CONSUMABLE_ITEM_SLOT = new HUDItemSlot("consumableItemSlot", HIERARCHY_INDEX,
                false, Color.WHITE);
        CONSUMABLE_ITEM_SLOT.locationAnchor = new Vector2i(1, 1);
        CONSUMABLE_ITEM_SLOT.position = new Vector3f(-160.0f, -90.0f, 0.0f);

        FLASK_ITEM_SLOT = new HUDItemSlot("flaskItemSlot", HIERARCHY_INDEX,
                true, new Vector4f(0.059f, 0.329f, 1.0f, 1.0f));
        FLASK_ITEM_SLOT.locationAnchor = new Vector2i(1, 1);
        FLASK_ITEM_SLOT.position = new Vector3f(-260.0f, -90.0f, 0.0f);
    }


}
