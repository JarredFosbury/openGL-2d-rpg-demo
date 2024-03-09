package game;

import engine.audio.SoundClip;
import engine.audio.SoundSource;
import engine.core.Entity;
import engine.core.EntityType;
import engine.core.MouseListener;
import engine.fontRendering.Font;
import engine.fontRendering.FontLoader;
import engine.rendering.Color;
import engine.rendering.ScreenSpaceSprite;
import engine.rendering.TextMesh;
import engine.rendering.Texture;
import org.joml.Vector2f;
import org.joml.Vector2i;
import org.joml.Vector3f;

public class MainMenuScene extends Entity
{
    private final Font defaultFont;
    private final SoundClip music;
    private final Texture pointSprite;

    private final TextMesh selectionIndicator;
    private final TextMesh startGameText;
    private final TextMesh optionsText;
    private final TextMesh quitText;

    private int cursorSelectionId;

    public MainMenuScene()
    {
        super("Main Menu Scene Handler", EntityType.ScriptableBehavior);

        defaultFont = FontLoader.loadFont("res/fonts/morris roman/morrisRoman.png", "res/fonts/morris roman/morrisRoman.fnt");
        music = new SoundClip("res/audio/music/mainMenuTheme.ogg");
        pointSprite = new Texture("res/textures/UI/square.png", false, true, false);

        SoundSource musicSource = new SoundSource("music-soundSource", music, true);
        musicSource.setVolume(0.5f);
        musicSource.play();

        TextMesh titleText = new TextMesh("title-text", defaultFont, true);
        titleText.fontSize_PIXELS = 48.0f;
        titleText.locationAnchor = new Vector2i(0, -1);
        titleText.textAlignment = new Vector2i(0, 0);
        titleText.position = new Vector3f(0.0f, 120.0f, 0.0f);
        titleText.text = "2D RPG DEMO";

        startGameText = new TextMesh("play-text", defaultFont, true);
        startGameText.fontSize_PIXELS = 32.0f;
        startGameText.textAlignment = new Vector2i(0, 0);
        startGameText.position = new Vector3f(0.0f, 50.0f, 0.0f);
        startGameText.text = "Start Game";

        optionsText = new TextMesh("options-text", defaultFont, true);
        optionsText.fontSize_PIXELS = 32.0f;
        optionsText.textAlignment = new Vector2i(0, 0);
        optionsText.position = new Vector3f(0.0f, 100.0f, 0.0f);
        optionsText.text = "Options";

        quitText = new TextMesh("quit-text", defaultFont, true);
        quitText.fontSize_PIXELS = 32.0f;
        quitText.textAlignment = new Vector2i(0, 0);
        quitText.position = new Vector3f(0.0f, 150.0f, 0.0f);
        quitText.text = "Quit To Desktop";

        selectionIndicator = new TextMesh("selection-text", defaultFont, true);
        selectionIndicator.fontSize_PIXELS = 32.0f;
        selectionIndicator.textAlignment = new Vector2i(0, 0);
        selectionIndicator.position = new Vector3f(0.0f, 0.0f, 0.0f);
        selectionIndicator.text = ">                        <";
    }

    public void update()
    {
        Vector2f cursor = new Vector2f(MouseListener.getPositionX(), MouseListener.getPositionY());
        int option1Y = (int)startGameText.getTrueScreenPosition().y;
        int option2Y = (int)optionsText.getTrueScreenPosition().y;
        int option3Y = (int)quitText.getTrueScreenPosition().y;
        int buttonSizeVertical_PX = 50;
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
    }
}