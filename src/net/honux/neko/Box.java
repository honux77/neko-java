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
    public static final int W = 320;
    public static final int H = 540;

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

    public int getFps() {
        return fps;
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

    public Input getInput() {
        return input;
    }

    public void update() {
        neko.update(frame);
    }

    private void init(String title) {
        thread = new Thread(this);
        input = new Input(this);
        neko = new Neko(this, DELAY, SCALE);
        neko.setPosition(W / 2, H / 2);
        mainWindow = new MainWindow(title, this);
        renderer = new Renderer(this);
        renderer.setBackground(new BxImage("./resources/bg.png"));
    }

    private void render() {
        //System.out.printf("frame: %d fps: %d\n", frame, fps);
        renderer.clear(Renderer.COLOR_BG);

        for (var coin: coins) {
            renderer.renderBxObject(coin, frame);
        }
        renderer.renderBxObject(neko, frame);

        mainWindow.render(frame, fps);
    }

    public void addOrRemoveCoin(int x, int y) {
        //smaller coin than Neko
        for (int i = 0; i < coins.size(); i++) {
            if (coins.get(i).atPosition(x, y)) {
                coins.remove(i);
                return;
            }
        }
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

    public MainWindow getWindow() {
        return mainWindow;
    }
}
