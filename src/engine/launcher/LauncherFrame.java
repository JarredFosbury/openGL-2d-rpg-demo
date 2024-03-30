package engine.launcher;

import javax.swing.*;
import java.awt.*;

public class LauncherFrame extends JFrame
{
    private final JButton launch_BUTTON;
    private final JComboBox resolution_COMBO;
    private final JLabel resolution_LABEL;
    private final JCheckBox vsync_CHECKBOX;
    private final JLabel vsync_LABEL;
    private final JCheckBox maximized_CHECKBOX;
    private final JLabel maximized_LABEL;
    private final JCheckBox borderless_CHECKBOX;
    private final JLabel borderless_LABEL;
    private final JComboBox msaa_COMBO;
    private final JLabel msaa_LABEL;

    private final String[] SUPPORTED_RESOLUTIONS;
    private final String[] MSAA_LEVELS;

    public LauncherFrame(int WIDTH, int HEIGHT)
    {
        this.setTitle("Launcher");
        this.setLayout(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(WIDTH, HEIGHT);
        this.setResizable(false);

        SUPPORTED_RESOLUTIONS = new String[] {
                "1176x664",
                "1280x720",
                "1360x768",
                "1366x768",
                "1834x768",
                "1600x900",
                "1920x1080",
                "2560x1440"
        };
        resolution_COMBO = new JComboBox(SUPPORTED_RESOLUTIONS);
        resolution_COMBO.setBounds(100, 15, 225, 30);

        {   // find default display-mode
            GraphicsDevice[] graphicsDevices = GraphicsEnvironment.getLocalGraphicsEnvironment().getScreenDevices();
            DisplayMode defaultMode = graphicsDevices[0].getDisplayMode();
            String[] defaultSliced = defaultMode.toString().split("x");

            for (int i = 0; i < SUPPORTED_RESOLUTIONS.length; i++)
            {
                if (SUPPORTED_RESOLUTIONS[i].split("x")[0].equals(defaultSliced[0]))
                {
                    resolution_COMBO.setSelectedIndex(i);
                    break;
                }
            }
        }

        this.add(resolution_COMBO);

        resolution_LABEL = new JLabel();
        resolution_LABEL.setText("Resolution");
        resolution_LABEL.setBounds(10, 15, WIDTH, 30);
        resolution_LABEL.setHorizontalAlignment(JLabel.LEFT);
        this.add(resolution_LABEL);

        vsync_CHECKBOX = new JCheckBox();
        vsync_CHECKBOX.setBounds(WIDTH/2 - 80, 50, 30, 30);
        this.add(vsync_CHECKBOX);

        vsync_LABEL = new JLabel();
        vsync_LABEL.setText("V-Sync");
        vsync_LABEL.setBounds(10, 50, WIDTH, 30);
        vsync_LABEL.setHorizontalAlignment(JLabel.LEFT);
        this.add(vsync_LABEL);

        maximized_CHECKBOX = new JCheckBox();
        maximized_CHECKBOX.setBounds(WIDTH/2 - 80, 85, 30, 30);
        this.add(maximized_CHECKBOX);

        maximized_LABEL = new JLabel();
        maximized_LABEL.setText("Fullscreen");
        maximized_LABEL.setBounds(10, 85, WIDTH, 30);
        maximized_LABEL.setHorizontalAlignment(JLabel.LEFT);
        this.add(maximized_LABEL);

        borderless_CHECKBOX = new JCheckBox();
        borderless_CHECKBOX.setBounds(WIDTH/2 - 80, 120, 30, 30);
        this.add(borderless_CHECKBOX);

        borderless_LABEL = new JLabel();
        borderless_LABEL.setText("Borderless");
        borderless_LABEL.setBounds(10, 120, WIDTH, 30);
        borderless_LABEL.setHorizontalAlignment(JLabel.LEFT);
        this.add(borderless_LABEL);

        MSAA_LEVELS = new String[] {
                "Off",
                "2x",
                "4x",
                "8x",
                "16x"
        };
        msaa_COMBO = new JComboBox(MSAA_LEVELS);
        msaa_COMBO.setBounds(100, 155, 225, 30);
        this.add(msaa_COMBO);

        msaa_LABEL = new JLabel();
        msaa_LABEL.setText("MSAA");
        msaa_LABEL.setBounds(10, 155, WIDTH, 30);
        msaa_LABEL.setHorizontalAlignment(JLabel.LEFT);
        this.add(msaa_LABEL);

        launch_BUTTON = new JButton();
        launch_BUTTON.setBounds(WIDTH/2 - 100, 215, 200, 30);
        launch_BUTTON.setText("Launch Game");
        launch_BUTTON.addActionListener(e -> LauncherManager.launchGame());
        this.add(launch_BUTTON);

        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }

    public int[] getSelectedResolution()
    {
        String[] sliced = SUPPORTED_RESOLUTIONS[resolution_COMBO.getSelectedIndex()].split("x");
        return new int[] {Integer.parseInt(sliced[0]), Integer.parseInt(sliced[1])};
    }

    public boolean[] getWindowSettings()
    {
        return new boolean[] {vsync_CHECKBOX.isSelected(), maximized_CHECKBOX.isSelected(), borderless_CHECKBOX.isSelected()};
    }

    public int getMSAA()
    {
        String value = MSAA_LEVELS[msaa_COMBO.getSelectedIndex()];
        switch (value)
        {
            case "Off":
                return 0;
            case "2x":
                return 1;
            case "4x":
                return 2;
            case "8x":
                return 3;
            case "16x":
                return 4;
        }

        return -1;
    }
}