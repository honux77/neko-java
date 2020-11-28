package net.honux.neko;

import com.sun.tools.javac.Main;

import java.util.ArrayList;
import java.util.List;

/**
 * Main App and container
 * also can be used to provide other resources
 */
public class Box implements Runnable {

    public static final double GAP = 1.0 / 60;
    public static final int DELAY = 60 / 2;
    public static final int SCALE = 2;

    private int fps;
    private int frame = 0;
    private Thread thread;
    private Input input;
    private Renderer renderer;
    private boolean running;

    private MainWindow mainWindow;

    private Neko neko;
    private List<Coin> coins = new ArrayList<>();


    public Box(String title) {
        init(title);
    }

    public List<Coin> getCoins() {
        return coins;
    }

    private void init(String title) {
        thread = new Thread(this);
        renderer = new Renderer(this);
        input = new Input(this);
        neko = new Neko(this, DELAY, SCALE);
        //mainWindow = new MainWindow(title, this);
        //mainWindow.addListener(input);
    }

    public int getFps() {
        return fps;
    }

    private void initOthers() {
        thread = new Thread(this);
    }


    public Neko getNeko() {
        return neko;
    }

    public void start() {
        thread.run();
    }

    public int getFrame() {
        return frame;
    }

    public void update() {
        renderer.update();
    }

    private void render() {
        renderer.render(true);
    }

    public void addCoin(int x, int y) {
        //smaller coin than Neko
        coins.add(new Coin(this, x, y, SCALE / 2));
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

    public static void main(String[] args) {
        Box box = new Box("Simple Neko App v0.2");
        box.start();
    }
}
