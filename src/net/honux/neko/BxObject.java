package net.honux.neko;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class BxObject {

    protected final Box box;
    protected int w, h, size;
    protected double x, y, dx, dy;
    protected double v = 0;

    private Map<Integer, BxImage> allImages = new HashMap<>();
    private Map<String, List<BxImage>> images = new HashMap<>();
    private List<String> allStatus = new ArrayList<>();
    protected int delay = 1;

    private int scale = 1;
    private int lastFrame;
    protected String status;

    public abstract void addStatusForImages();

    public abstract void addStatusImages();

    public abstract void update(int frame);

    public boolean collideWith(BxObject other) {
        int sx = (int) x;
        int sy = (int) y;
        int dx = (int) other.x;
        int dy = (int) other.y;
        return (sx - dx) * (sx - dx) + (sy - dy) * (sy - dy) <= size * size + other.size * other.size;
    }

    public BxObject(Box box, int delay, int scale) {
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
        w = allImages.get(1).getW();
        h = allImages.get(1).getH();
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

    public BxImage getImage(int frame) {
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
            allImages.put(idx, new BxImage(dirname + "/" + img,  scale));
        }
    }

    public boolean atPosition(int x, int y) {
        return (x - this.x) * (x - this.x) + (y - this.y) * (y - this.y) < size * size * 1.44;
    }

    @Override
    public String toString() {
        return "BxObject{" +
                "x=" + x +
                ", y=" + y +
                '}';
    }
}


