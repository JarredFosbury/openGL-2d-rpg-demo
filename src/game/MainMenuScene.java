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
import org.joml.Vector2f;
import org.joml.Vector2i;
import org.joml.Vector3f;
import org.joml.Vector4f;

public class MainMenuScene extends Entity
{
    private final TextMesh titleText;
    private final TextMesh startGameText;
    private final TextMesh optionsText;
    private final TextMesh quitText;
    private final SoundSource musicSource;
    private final SoundSource buttonHoverSfx;
    private final SoundSource buttonClickSfx;
    private final ScreenSpaceSprite buttonBackdrop;

    private final TextMesh backToMainText;

    private boolean inSettingsMenu;
    private int cursorSelectionId;
    private Vector2f cursor;

    private final int buttonSizeVertical_PX;
    private final Vector4f defaultButtonColor;
    private final Vector4f hoverButtonColor;
    private final String mainMenuTitle;
    private final String settingsMenuTitle;

    public MainMenuScene()
    {
        super("Main Menu Scene Handler", EntityType.ScriptableBehavior);

        inSettingsMenu = false;
        cursorSelectionId = 0;

        buttonSizeVertical_PX = 50;
        defaultButtonColor = new Vector4f(0.797f, 0.797f, 0.797f, 1.0f);
        hoverButtonColor = new Vector4f(1.0f);
        mainMenuTitle = "2D RPG DEMO";
        settingsMenuTitle = "Settings";

        Scene.assets.addAssetToPool(FontLoader.loadFont("res/fonts/morris roman/morrisRoman.png",
                "res/fonts/morris roman/morrisRoman.fnt"), "morrisRoman");
        Scene.assets.addAssetToPool(new SoundClip("res/audio/music/mainMenuTheme.ogg"), "mainMenuTheme");
        Scene.assets.addAssetToPool(new SoundClip("res/audio/sfx/interfaceClick01.ogg"), "interfaceClick01");
        Scene.assets.addAssetToPool(new SoundClip("res/audio/sfx/interfaceClick02.ogg"), "interfaceClick02");
        Scene.assets.addAssetToPool(new Texture("res/textures/blackLeather_albedo.png", true, true, true), "blackLeather_albedo");
        Scene.assets.addAssetToPool(new Texture("res/textures/UI/heart.png", true, false, true), "heart");

        musicSource = new SoundSource("music-soundSource", (SoundClip) Scene.assets.getAssetFromPool("mainMenuTheme"), true);
        musicSource.setVolume(0.5f);
        musicSource.play();

        buttonHoverSfx = new SoundSource("buttonHoverSfx-soundSource", (SoundClip) Scene.assets.getAssetFromPool("interfaceClick01"), false);
        buttonHoverSfx.setVolume(0.1f);

        buttonClickSfx = new SoundSource("buttonClickSfx-soundSource", (SoundClip) Scene.assets.getAssetFromPool("interfaceClick02"), false);
        buttonClickSfx.setVolume(0.3f);

        titleText = new TextMesh("title-text", (Font) Scene.assets.getAssetFromPool("morrisRoman"), true);
        titleText.fontSize_PIXELS = 48.0f;
        titleText.locationAnchor = new Vector2i(0, -1);
        titleText.textAlignment = new Vector2i(0, 0);
        titleText.position = new Vector3f(0.0f, 120.0f, 0.0f);
        titleText.text = "2D RPG DEMO";

        buttonBackdrop = new ScreenSpaceSprite("buttonBg-sprite", Scene.screenSpace2dShader,"blackLeather_albedo", Color.WHITE, true);
        buttonBackdrop.scale = new Vector3f(500.0f, 200.0f, 1.0f);
        buttonBackdrop.position = new Vector3f(0.0f, 130.0f, 0.0f);

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

        backToMainText = new TextMesh("back-text", (Font) Scene.assets.getAssetFromPool("morrisRoman"), true);
        backToMainText.fontSize_PIXELS = 32.0f;
        backToMainText.textAlignment = new Vector2i(0, 0);
        backToMainText.locationAnchor = new Vector2i(0, 1);
        backToMainText.position = new Vector3f(0.0f, -100.0f, 0.0f);
        backToMainText.text = "Back";
    }

    public void update()
    {
        // doing this for convenience, typing out MouseListener.getPosition everytime is annoying
        cursor = new Vector2f(MouseListener.getPositionX(), MouseListener.getPositionY());

        startGameText.isVisible = !inSettingsMenu;
        optionsText.isVisible = !inSettingsMenu;
        quitText.isVisible = !inSettingsMenu;
        buttonBackdrop.isVisible = !inSettingsMenu;

        backToMainText.isVisible = inSettingsMenu;

        if (!inSettingsMenu)
            updateMainMenu();
        else
            updateSettingsMenu();
    }

    private void updateMainMenu()
    {
        int lastSelectedButton = cursorSelectionId;
        int option1Y = (int)startGameText.getTrueScreenPosition().y;
        int option2Y = (int)optionsText.getTrueScreenPosition().y;
        int option3Y = (int)quitText.getTrueScreenPosition().y;
        if (cursor.y > option1Y && cursor.y < option1Y + buttonSizeVertical_PX)
            cursorSelectionId = 0;

        if (cursor.y > option2Y && cursor.y < option2Y + buttonSizeVertical_PX)
            cursorSelectionId = 1;

        if (cursor.y > option3Y && cursor.y < option3Y + buttonSizeVertical_PX)
            cursorSelectionId = 2;

        startGameText.colorRGBA = (cursorSelectionId == 0 ? hoverButtonColor : defaultButtonColor);
        optionsText.colorRGBA = (cursorSelectionId == 1 ? hoverButtonColor : defaultButtonColor);
        quitText.colorRGBA = (cursorSelectionId == 2 ? hoverButtonColor : defaultButtonColor);
        titleText.text = mainMenuTitle;

        if (cursorSelectionId != lastSelectedButton)
            buttonHoverSfx.play();

        if (MouseListener.isButtonPressed(0))
        {
            int halfWidth = GlobalSettings.windowWidth / 2;
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

    private void updateSettingsMenu()
    {
        titleText.text = settingsMenuTitle;
        int lastSelectedButton = cursorSelectionId;
        int option1Y = (int)backToMainText.getTrueScreenPosition().y;
        if (cursor.y > option1Y && cursor.y < option1Y + buttonSizeVertical_PX)
            cursorSelectionId = 0;

        backToMainText.colorRGBA = (cursorSelectionId == 0 ? hoverButtonColor : defaultButtonColor);

        if (cursorSelectionId != lastSelectedButton)
            buttonHoverSfx.play();

        if (MouseListener.isButtonPressed(0))
        {
            int halfWidth = GlobalSettings.windowWidth / 2;
            int horizontalButtonBounds_PX = 150;
            if (cursor.x < halfWidth + horizontalButtonBounds_PX && cursor.x > halfWidth - horizontalButtonBounds_PX)
            {
                switch (cursorSelectionId)
                {
                    case 0 -> { if (cursor.y > option1Y && cursor.y < option1Y + buttonSizeVertical_PX) { toggleSettingsMenu(); }}
                }
            }
        }
    }

    private void startGame()
    {
        buttonClickSfx.play();
        unloadScene();
        new GameScene();
    }

    private void toggleSettingsMenu()
    {
        buttonClickSfx.play();
        cursorSelectionId = -1;
        inSettingsMenu = !inSettingsMenu;
    }

    private void quitGame()
    {
        buttonClickSfx.play();
        unloadScene();
        Window.get().queueWindowTermination();
    }

    public void unloadScene()
    {
        musicSource.stop();
        buttonHoverSfx.stop();

        Scene.assets.releaseAllAssetsFromPool();
        Scene.entities.clear();
    }
}