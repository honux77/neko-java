package net.honux.neko;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;
import java.awt.*;

public class MainWindow extends JFrame {

    private final String TITLE = "Neko App";
    public final static int SCALE = 2;
    public static final double GAP = 1.0 / 60;
    public static final int DELAY = 60 / 2;

    private List<Coin> coins = new ArrayList<>();
    private int frame = 0;
    private Box box;

    private BufferedImage image;
    private Canvas canvas;
    private Graphics graphics;

    public MainWindow(String title, Box box) {
        this.box = box;
        initResource();
        initFrame(title);
        addEventListener(box.getInput());
    }

    private void initResource() {
        Dimension d = new Dimension(box.W, box.H);
        image = new BufferedImage(box.W, box.H, BufferedImage.TYPE_INT_RGB);
        canvas = new Canvas();
        canvas.setPreferredSize(d);
        canvas.setMaximumSize(d);
        canvas.setMinimumSize(d);
    }

    public int[] getDataBufferInt() {
        return ((DataBufferInt) image.getRaster().getDataBuffer()).getData();
    }

    public void render(int fps, long frame) {
        graphics.drawImage(image, 0, 0, canvas.getWidth(), canvas.getHeight(), this);
        graphics.drawString(frame + " : " + fps, 20, 20);
        canvas.getBufferStrategy().show();
    }

    private void initFrame(String title) {
        setTitle(title);
        setPreferredSize(new Dimension(box.W, box.H));
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        add(canvas, BorderLayout.CENTER);
        pack();
        setLocationRelativeTo(null);
        setResizable(false);
        setVisible(true);

        canvas.createBufferStrategy(2);
        graphics = canvas.getBufferStrategy().getDrawGraphics();
    }

    public void addEventListener(Input input) {
        canvas.addMouseListener(input);
        canvas.addMouseMotionListener(input);
    }
}
