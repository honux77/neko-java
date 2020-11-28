package net.honux.neko;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;
import java.awt.*;

public class MainWindow extends JFrame {

    private final String TITLE = "Neko App";
    public final static int SCALE = 2;
    public static final double GAP = 1.0 / 60;
    public static final int DELAY = 60 / 2;

    private int fps;
    private Neko neko;
    private List<Coin> coins = new ArrayList<>();
    private int frame = 0;
    private Thread thread;
    private Input input;
    private Renderer renderer;
    private Box box;
    private boolean running;

    private BufferedImage image;
    private Canvas canvas;
    private Dimension dimension;
    private Graphics graphics;

    public MainWindow(String title, Box box) {
        this.box = box;
    }

//    private void initResource(Box box, Graphics g) {
//        image = new BufferedImage(g.getWidth(), g.getHeight(), BufferedImage.TYPE_INT_RGB);
//        canvas = new Canvas();
//        canvas.setPreferredSize(dimension);
//        canvas.setMaximumSize(dimension);
//        canvas.setMinimumSize(dimension);
//    }

    private void initUI() {
        setTitle(TITLE);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        addMouseListener(input);
        addMouseMotionListener(input);
        setLocationRelativeTo(null);
        setResizable(false);
        neko.setPosition(getWidth() / 2, getHeight() / 2);

    }

    public void addListener(Input input) {
        addMouseListener(input);
        addMouseMotionListener(input);
    }
}
