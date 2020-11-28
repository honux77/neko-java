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

public class Renderer {
    private int H;
    private int W;
    private int[] buffer;

    public Renderer(Box box) {
        H = box.H;
        W = box.W;
        buffer = box.getWindow().getDataBufferInt();
    }

    private void setPixel(int x, int y, int color, int alpha) {
        if (x < 0 || x >= W || y < 0 || y >= H || color == alpha) return;
        buffer[x + y * W] = color;
    }

    public void renderImage(BxImage image, int sx, int sy, int alpha) {
        if (alpha == -1) {
            alpha = image.getPixel(0, 0);
        }
        for (int y = 0; y < image.getH(); y++) {
            for (int x = 0; x < image.getW(); x++) {
                setPixel(sx + x, sy + y, image.getPixel(x, y), alpha);
            }
        }
    }

    public void clear(int color) {
        for (int i = 0; i < buffer.length; i++) {
            buffer[i] = 0xFFFFFFFF;
        }
    }
}


