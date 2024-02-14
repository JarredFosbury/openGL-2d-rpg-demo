package engine.launcher;

import javax.swing.*;

public class LauncherFrame extends JFrame
{
    JButton launch_BUTTON = new JButton();

    public LauncherFrame(int WIDTH, int HEIGHT)
    {
        this.setTitle("Launcher");
        this.setLayout(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(WIDTH, HEIGHT);
        this.setResizable(false);
        this.setVisible(true);

        launch_BUTTON.setBounds(200, 350, 200, 30);
        launch_BUTTON.setText("Launch Game");
        launch_BUTTON.addActionListener(e -> LauncherManager.launchGame());
        this.add(launch_BUTTON);
    }
}