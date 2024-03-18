package game;

import engine.core.Entity;
import engine.core.EntityType;
import engine.core.Time;
import engine.core.Utils;
import engine.rendering.ScreenSpaceSprite;
import org.joml.Vector2i;
import org.joml.Vector3f;
import org.joml.Vector4f;

public class HUDBar extends Entity
{
    public Vector2i locationAnchor;
    public float barValue;
    public float chaseBarFollowDelay_SECONDS;
    public float chaseBarSpeedMultiplier;

    private final ScreenSpaceSprite mainBar;
    private final ScreenSpaceSprite barBack;
    private ScreenSpaceSprite chaseBar;
    private final boolean hasChaseBar;
    private final float barWidth;
    private final float barHeight;
    private float chaseBarValue;
    private float lastStoredBarValue;
    private float timeTillChaseBarUpdate;

    public HUDBar(String name, int HIERARCHY_INDEX, boolean hasChaseBar, Vector4f mainBarColor, Vector4f chaseBarColor, float barWidth, float barHeight)
    {
        super(name, EntityType.ScriptableBehavior, HIERARCHY_INDEX);
        this.hasChaseBar = hasChaseBar;
        this.barValue = 1.0f;
        this.chaseBarValue = 1.0f;
        this.locationAnchor = new Vector2i(0);
        this.chaseBarFollowDelay_SECONDS = 1.0f;
        this.barWidth = barWidth;
        this.barHeight = barHeight;
        this.lastStoredBarValue = barValue;
        this.timeTillChaseBarUpdate = 0.0f;
        this.chaseBarSpeedMultiplier = 0.5f;
        barBack = new ScreenSpaceSprite(name + "_barBack", HIERARCHY_INDEX,
                "barSwatch32px", new Vector4f(0.173f, 0.173f, 0.173f, 1.0f), true);
        barBack.scale = new Vector3f(barWidth, barHeight, 1.0f);

        mainBar = new ScreenSpaceSprite(name + "_barMain", HIERARCHY_INDEX + 2,
                "barSwatch32px", mainBarColor, true);
        mainBar.scale = new Vector3f(barWidth, barHeight, 1.0f);

        if (!hasChaseBar)
            return;

        chaseBar = new ScreenSpaceSprite(name + "_barChase", HIERARCHY_INDEX + 1,
                "barSwatch32px", chaseBarColor, true);
        chaseBar.scale = new Vector3f(barWidth, barHeight, 1.0f);
    }

    public void update()
    {
        barValue = Utils.clamp01(barValue);
        mainBar.locationAnchor = locationAnchor;
        barBack.locationAnchor = locationAnchor;
        mainBar.scale = new Vector3f(barWidth * barValue, barHeight, 1.0f);
        mainBar.position = new Vector3f(position).add(new Vector3f(mainBar.scale.x/2.0f - barWidth/2.0f, 0.0f, 0.0f));
        barBack.position = position;

        if (!hasChaseBar)
            return;

        chaseBar.locationAnchor = locationAnchor;
        chaseBar.scale = new Vector3f(barWidth * chaseBarValue, barHeight, 1.0f);
        chaseBar.position = new Vector3f(position).add(new Vector3f(chaseBar.scale.x/2.0f - barWidth/2.0f, 0.0f, 0.0f));

        if (lastStoredBarValue != barValue)
        {
            lastStoredBarValue = barValue;
            timeTillChaseBarUpdate = chaseBarFollowDelay_SECONDS;
        }

        if (timeTillChaseBarUpdate > 0.0f)
        {
            timeTillChaseBarUpdate -= Time.deltaTime;
        }
        else
        {
            if (chaseBarValue > barValue)
                chaseBarValue -= Time.deltaTime * chaseBarSpeedMultiplier;
            else if (chaseBarValue < barValue)
                chaseBarValue = barValue;
        }
    }
}