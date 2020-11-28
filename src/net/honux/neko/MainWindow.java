package net.honux.neko;

import java.util.ArrayList;
import java.util.List;
import javax.swing.*;
import java.awt.*;

public class MainWindow extends JFrame implements Runnable {

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
    private boolean running;

    public MainWindow() {
        initObjects();
        initComponent();
        initUI();
        initOthers();
    }

    public Input getInput() {
        return input;
    }

    public List<Coin> getCoins() {
        return coins;
    }

    public int getFps() {
        return fps;
    }

    private void initComponent() {
        renderer = new Renderer(this);
        input = new Input(this);

    }

    private void initOthers() {
        thread = new Thread(this);
    }

    private void initObjects() {
        neko = new Neko(this, DELAY, SCALE);
    }

    private void initUI() {
        setTitle(TITLE);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        add(renderer, BorderLayout.CENTER);
        setSize(renderer.getSize());
        addMouseListener(input);
        addMouseMotionListener(input);
        setLocationRelativeTo(null);
        setResizable(false);
        neko.setPosition(getWidth() / 2, getHeight() / 2);

    }

    public Neko getNeko() {
        return neko;
    }

    public void start() {
        thread.start();
    }

    public int getFrame() {
        return frame;
    }

    public void update() {
        renderer.update();
    }

    private void render() {
        renderer.render();
    }

    public static void main(String[] args) {
        MainWindow mainWindow = new MainWindow();
        mainWindow.setVisible(true);
        mainWindow.start();
    }

    @Override
    public void run() {
        running = true;
        boolean render = false;
        final double OB = 1000_000_000.0f;
        double now;
        double lastTime = System.nanoTime() / OB;
        double passedTime;
        double frameTime = 0;
        double unprocessedTime = 0;
        int lastFrame = 0;

        while (running) {
            now = System.nanoTime() / OB;
            passedTime = now - lastTime;
            unprocessedTime += passedTime;
            frameTime += passedTime;
            lastTime = now;

            if (frameTime >= 1.0) {
                fps = frame - lastFrame;
                lastFrame = frame;
                frameTime = 0;
            }

            while (unprocessedTime > GAP) {
                unprocessedTime -= GAP;
                render = true;
                frame++;
                update();
            }

            if (render) {
                render = false;
                render();
            } else {
                try {
                    Thread.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

        }
    }

    public void addCoin(int x, int y) {
        //smaller coin than Neko
        coins.add(new Coin(this, x, y, SCALE / 2));
    }
}
