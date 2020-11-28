package net.honux.neko;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class BxImage {

    private int w, h, scale = 1;
    private int[] buffer;

    public int getW() {
        return w * scale;
    }

    public int getH() {
        return h * scale;
    }

    public int[] getBuffer() {
        return buffer;
    }

    public int getPixel(int x, int y) {
        int ox = x / scale;
        int oy = y / scale;
        return buffer[ox + oy * h];
    }

    public BxImage(String path, int scale) {
        this.scale = scale;
        loadImage(path);
    }

    public BxImage(String path) {
        loadImage(path);
    }

    private void loadImage(String path) {
        try {
            BufferedImage image = ImageIO.read(new File(path));
            w = image.getWidth();
            h = image.getHeight();
            buffer = image.getRGB(0, 0, w, h, null, 0, w);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}