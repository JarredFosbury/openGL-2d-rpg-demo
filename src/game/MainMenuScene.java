package game;

import engine.audio.SoundClip;
import engine.audio.SoundSource;
import engine.core.*;
import engine.fontRendering.Font;
import engine.fontRendering.FontLoader;
import engine.rendering.Color;
import engine.rendering.ScreenSpaceSprite;
import engine.rendering.TextMesh;
import engine.rendering.Texture;
import engine.shaders.ScreenSpace2dShader;
import engine.shaders.Standard2dShader;
import org.joml.Vector2f;
import org.joml.Vector2i;
import org.joml.Vector3f;

public class MainMenuScene extends Entity
{
    private final TextMesh selectionIndicator;
    private final TextMesh startGameText;
    private final TextMesh optionsText;
    private final TextMesh quitText;
    private final SoundSource musicSource;
    private final SoundSource interfaceSfx01;

    private int cursorSelectionId;
    private final int buttonSizeVertical_PX;

    public MainMenuScene()
    {
        super("Main Menu Scene Handler", EntityType.ScriptableBehavior);

        cursorSelectionId = 0;
        buttonSizeVertical_PX = 50;

        Scene.assets.addAssetToPool(FontLoader.loadFont("res/fonts/morris roman/morrisRoman.png",
                "res/fonts/morris roman/morrisRoman.fnt"), "morrisRoman");
        Scene.assets.addAssetToPool(new SoundClip("res/audio/music/mainMenuTheme.ogg"), "mainMenuTheme");
        Scene.assets.addAssetToPool(new SoundClip("res/audio/sfx/interfaceClick01.ogg"), "interfaceClick01");
        Scene.assets.addAssetToPool(new Texture("res/textures/blackLeather_albedo.png", true, true, true), "blackLeather_albedo");
        Scene.assets.addAssetToPool(new Texture("res/textures/UI/heart.png", true, false, true), "heart");

        musicSource = new SoundSource("music-soundSource", (SoundClip) Scene.assets.getAssetFromPool("mainMenuTheme"), true);
        musicSource.setVolume(0.5f);
        musicSource.play();

        TextMesh titleText = new TextMesh("title-text", (Font) Scene.assets.getAssetFromPool("morrisRoman"), true);
        titleText.fontSize_PIXELS = 48.0f;
        titleText.locationAnchor = new Vector2i(0, -1);
        titleText.textAlignment = new Vector2i(0, 0);
        titleText.position = new Vector3f(0.0f, 120.0f, 0.0f);
        titleText.text = "2D RPG DEMO";

        startGameText = new TextMesh("play-text", (Font) Scene.assets.getAssetFromPool("morrisRoman"), true);
        startGameText.fontSize_PIXELS = 32.0f;
        startGameText.textAlignment = new Vector2i(0, 0);
        startGameText.position = new Vector3f(0.0f, 50.0f, 0.0f);
        startGameText.text = "Start Game";

        optionsText = new TextMesh("options-text", (Font) Scene.assets.getAssetFromPool("morrisRoman"), true);
        optionsText.fontSize_PIXELS = 32.0f;
        optionsText.textAlignment = new Vector2i(0, 0);
        optionsText.position = new Vector3f(0.0f, 100.0f, 0.0f);
        optionsText.text = "Options";

        quitText = new TextMesh("quit-text", (Font) Scene.assets.getAssetFromPool("morrisRoman"), true);
        quitText.fontSize_PIXELS = 32.0f;
        quitText.textAlignment = new Vector2i(0, 0);
        quitText.position = new Vector3f(0.0f, 150.0f, 0.0f);
        quitText.text = "Quit To Desktop";

        selectionIndicator = new TextMesh("selection-text", (Font) Scene.assets.getAssetFromPool("morrisRoman"), true);
        selectionIndicator.fontSize_PIXELS = 32.0f;
        selectionIndicator.textAlignment = new Vector2i(0, 0);
        selectionIndicator.position = new Vector3f(0.0f, 0.0f, 0.0f);
        selectionIndicator.text = ">                        <";

        interfaceSfx01 = new SoundSource("interfaceSfx01-soundSource", (SoundClip) Scene.assets.getAssetFromPool("interfaceClick01"), false);
        interfaceSfx01.setVolume(0.1f);

        String[] assets = Scene.assets.getAllAssetKeys();
        for (String asset : assets) System.out.println(asset);
    }

    public void update()
    {
        int lastSelectedButton = cursorSelectionId;
        Vector2f cursor = new Vector2f(MouseListener.getPositionX(), MouseListener.getPositionY());
        int option1Y = (int)startGameText.getTrueScreenPosition().y;
        int option2Y = (int)optionsText.getTrueScreenPosition().y;
        int option3Y = (int)quitText.getTrueScreenPosition().y;
        if (cursor.y > option1Y && cursor.y < option1Y + buttonSizeVertical_PX)
            cursorSelectionId = 0;

        if (cursor.y > option2Y && cursor.y < option2Y + buttonSizeVertical_PX)
            cursorSelectionId = 1;

        if (cursor.y > option3Y && cursor.y < option3Y + buttonSizeVertical_PX)
            cursorSelectionId = 2;

        switch (cursorSelectionId)
        {
            case 0 -> selectionIndicator.position = new Vector3f(0.0f, startGameText.position.y,  0.0f);
            case 1 -> selectionIndicator.position = new Vector3f(0.0f, optionsText.position.y,  0.0f);
            case 2 -> selectionIndicator.position = new Vector3f(0.0f, quitText.position.y,  0.0f);
        }

        if (cursorSelectionId != lastSelectedButton)
            interfaceSfx01.play();

        if (MouseListener.isButtonPressed(0))
        {
            int halfWidth = GlobalSettings.WINDOW_WIDTH / 2;
            int horizontalButtonBounds_PX = 150;
            if (cursor.x < halfWidth + horizontalButtonBounds_PX && cursor.x > halfWidth - horizontalButtonBounds_PX)
            {
                switch (cursorSelectionId)
                {
                    case 0 -> { if (cursor.y > option1Y && cursor.y < option1Y + buttonSizeVertical_PX) { startGame(); }}
                    case 1 -> { if (cursor.y > option2Y && cursor.y < option2Y + buttonSizeVertical_PX) { toggleSettingsMenu(); }}
                    case 2 -> { if (cursor.y > option3Y && cursor.y < option3Y + buttonSizeVertical_PX) { quitGame(); }}
                }
            }
        }
    }

    private void startGame()
    {
        unloadScene();
        new GameScene();
    }

    private void toggleSettingsMenu()
    {}

    private void quitGame()
    {
        unloadScene();
        Window.get().queueWindowTermination();
    }

    public void unloadScene()
    {
        musicSource.stop();
        interfaceSfx01.stop();

        Scene.assets.releaseAllAssetsFromPool();
        Scene.entities.clear();
    }
}