package net.honux.neko;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * The Class which have responsibility to draw all GameObjects
 */
public class Renderer extends JPanel {

    private MainWindow mainWindow;
    private BufferedImage background;
    private long frame = 0;

    public Renderer(MainWindow window) {
        this.mainWindow = window;
        setupBackGround();
    }

    //Load bg image and setup panel
    private void setupBackGround() {
        try {
            background = ImageIO.read(new File("./resources/bg.png"));
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(new JFrame(), "BG loading error: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
        setDoubleBuffered(true);
        setSize(background.getWidth(), background.getHeight());
    }

    @Override
    public void paintComponents(Graphics g) {
        if (g == null) return;

        Neko neko = mainWindow.getNeko();
        g.drawImage(background, 0, 0, mainWindow);
        drawCoins(g);
        drawImageByScale(g, neko);
        g.drawString("frame: " + mainWindow.getFrame() + " fps: " + mainWindow.getFps(), 10, 10);
    }

    private void drawImageByScale(Graphics g, GameObject go) {
        int x = go.getX() - go.getW() * go.getScale() / 2;
        int y = go.getY() - go.getH() * go.getScale();
        int w = go.getW();
        int h = go.getH();
        if (go.getScale() == 1) {
            g.drawImage(go.getImage(mainWindow.getFrame()), x, y, this);
        } else {
            g.drawImage(go.getImage(mainWindow.getFrame()), x, y, x + w * go.getScale(), y + h * go.getScale(),
                    0, 0, w, h, mainWindow);
        }
    }

    private void drawCoins(Graphics g) {
        for(var coin: mainWindow.getCoins()) drawImageByScale(g, coin);
    }

    public void update() {
        int frame = mainWindow.getFrame();
        System.out.println(frame);

        //Update Neko
        Neko neko = mainWindow.getNeko();
        neko.update(frame);

        //Uodate coin after Neko
        var coins = mainWindow.getCoins();
        for (var coin: coins) coin.update(frame);
    }

    public void render() {
        paintComponents(this.getGraphics());
    }
}
