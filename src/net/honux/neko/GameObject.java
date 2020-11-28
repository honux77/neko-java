package net.honux.neko;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class GameObject {

    protected final Box box;
    protected int w, h, size;
    protected double x, y, dx, dy;
    private Map<Integer, BufferedImage> allImages = new HashMap<>();
    private Map<String, List<BufferedImage>> images = new HashMap<>();
    private List<String> allStatus = new ArrayList<>();
    protected int delay = 1;

    private int scale =  1;
    private int lastFrame;
    protected String status;

    public abstract void addStatusForImages();

    public abstract void addStatusImages();

    public abstract void update(int frame);

    public boolean collideWith(GameObject other) {
        int sx = (int) x;
        int sy= (int) y;
        int dx = (int) other.x;
        int dy = (int) other.y;
        return (sx - dx) * (sx - dx) + (sy - dy) * (sy - dy) <= size * size + other.size * other.size;
    }

    public int getScale() {
        return scale;
    }

    public GameObject(Box box, int delay, int scale) {
        this.box = box;
        this.delay = delay;
        this.scale = scale;
        loadAllImages();
        setWH();
        addStatusForImages();
        for (var s : allStatus) {
            images.put(s, new ArrayList<>());
        }
        addStatusImages();
    }

    public void setStatus(String status, int frame) {
        this.status = status;
        this.lastFrame = frame;
    }

    public int getLastFrame() {
        return lastFrame;
    }

    private void setWH() {
        w = allImages.get(1).getWidth();
        h = allImages.get(1).getHeight();
        size = w / 2 * scale;
    }

    public int getW() {
        return w;
    }

    public int getH() {
        return h;
    }

    public int getX() {
        return (int) x;
    }

    public int getY() {
        return (int) y;
    }

    public void setPosition(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getStatusImageSize(String status) {
        return images.get(status).size();
    }

    protected void addStatus(String... status) {
        for (String s : status) {
            this.allStatus.add(s);
        }
    }

    protected void setStatusImages(String status, int... num) {
        for (var i : num) {
            images.get(status).add(allImages.get(i));
        }
    }

    public BufferedImage getImage(int frame) {
        var subImages = images.get(status);
        return subImages.get(frame / delay % subImages.size());
    }

    private void loadAllImages() {
        final String path = "./resources/";
        String[] cname = getClass().getName().split("\\.");
        String dirname = path + cname[cname.length - 1].toLowerCase() + "/";
        File dir = new File(dirname);
        for (var img : dir.list()) {
            int idx = Integer.parseInt(img.split("\\.")[0]);
            allImages.put(idx, readImage(dirname + "/" + img));
        }
    }

    private BufferedImage readImage(String path) {
        try {
            return ImageIO.read(new File(path));
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(new JFrame(), e.getMessage(), "Image Load Error", JOptionPane.ERROR_MESSAGE);
            System.exit(1);
            return null;
        }
    }
}

