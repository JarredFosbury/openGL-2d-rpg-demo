package game;

import engine.core.Entity;
import engine.core.EntityType;
import engine.core.KeyListener;
import engine.core.Scene;
import engine.fontRendering.Font;
import engine.fontRendering.FontLoader;
import engine.rendering.TextMesh;
import org.joml.Vector2i;
import org.joml.Vector3f;

import static org.lwjgl.glfw.GLFW.*;

public class GameScene extends Entity
{
    public GameScene()
    {
        super("Game Scene Handler", EntityType.ScriptableBehavior);

        Scene.assets.addAssetToPool(FontLoader.loadFont("res/fonts/morris roman/morrisRoman.png",
                "res/fonts/morris roman/morrisRoman.fnt"), "defaultFont-morrisRoman");

        TextMesh titleText = new TextMesh("title-text", (Font) Scene.assets.getAssetFromPool("defaultFont-morrisRoman"), true);
        titleText.fontSize_PIXELS = 32.0f;
        titleText.locationAnchor = new Vector2i(0, 0);
        titleText.textAlignment = new Vector2i(0, 0);
        titleText.position = new Vector3f(0.0f);
        titleText.text = "Imagine gameplay here!";
    }
}