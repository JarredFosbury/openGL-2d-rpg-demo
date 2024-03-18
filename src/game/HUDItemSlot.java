package game;

import engine.core.Entity;
import engine.core.EntityType;
import engine.core.Utils;
import engine.rendering.Color;
import engine.rendering.ScreenSpace9SliceSprite;
import engine.rendering.ScreenSpaceSprite;
import org.joml.Vector2i;
import org.joml.Vector3f;
import org.joml.Vector4f;

public class HUDItemSlot extends Entity
{
    public Vector2i locationAnchor;
    public float conditionBarValue;

    private final ScreenSpace9SliceSprite itemSlotWeapon;
    private final ScreenSpace9SliceSprite itemSlotWeaponBack;
    private ScreenSpaceSprite weaponConditionBarBack;
    private ScreenSpaceSprite weaponConditionBar;
    private final boolean hasConditionBar;

    public HUDItemSlot(String name, int HIERARCHY_INDEX, boolean hasConditionBar, Vector4f conditionBarColor)
    {
        super(name, EntityType.ScriptableBehavior, HIERARCHY_INDEX);
        this.hasConditionBar = hasConditionBar;
        this.locationAnchor = new Vector2i(0);
        this.conditionBarValue = 1.0f;
        itemSlotWeapon = new ScreenSpace9SliceSprite(name + "_slotDetail", HIERARCHY_INDEX + 1,
                "itemSlot", Color.WHITE, true, new Vector4f(32.0f));
        itemSlotWeapon.scale = new Vector3f(100.0f, 130.0f, 1.0f);

        itemSlotWeaponBack = new ScreenSpace9SliceSprite(name + "_slotBack", HIERARCHY_INDEX,
                "button", Color.BLACK, true, new Vector4f(32.0f));
        itemSlotWeaponBack.scale = new Vector3f(100.0f, 130.0f, 1.0f);

        if (!hasConditionBar)
            return;

        weaponConditionBarBack = new ScreenSpaceSprite(name + "_barBack", HIERARCHY_INDEX,
                "barSwatch32px", new Vector4f(0.173f, 0.173f, 0.173f, 1.0f), true);
        weaponConditionBarBack.position = new Vector3f(0.0f, 70.0f, 0.0f);
        weaponConditionBarBack.scale = new Vector3f(80.0f, 10.0f, 1.0f);

        weaponConditionBar = new ScreenSpaceSprite(name + "_bar", HIERARCHY_INDEX + 1,
                "barSwatch32px", conditionBarColor, true);
        weaponConditionBar.position = new Vector3f(0.0f, 70.0f, 0.0f);
        weaponConditionBar.scale = new Vector3f(80.0f, 10.0f, 1.0f);
    }

    public void update()
    {
        itemSlotWeapon.position = position;
        itemSlotWeaponBack.position = position;
        itemSlotWeapon.locationAnchor = locationAnchor;
        itemSlotWeaponBack.locationAnchor = locationAnchor;

        if (!hasConditionBar)
            return;

        conditionBarValue = Utils.clamp01(conditionBarValue);
        weaponConditionBar.scale = new Vector3f(80.0f * conditionBarValue, 10.0f, 1.0f);
        weaponConditionBar.position = new Vector3f(position).add(new Vector3f((weaponConditionBar.scale.x/2.0f) - 40.0f, 70.0f, 0.0f));

        weaponConditionBarBack.position = new Vector3f(position).add(new Vector3f(0.0f, 70.0f, 0.0f));
        weaponConditionBarBack.locationAnchor = locationAnchor;
        weaponConditionBar.locationAnchor = locationAnchor;
    }
}