package net.honux.neko;

/**
 * The Class which have responsibility to draw all GameObjects
 */

public class Renderer {
    public final static int AUTO_ALPHA = -1;
    public final static int NO_ALPHA = -2;
    public final static int COLOR_BG = -3;


    private int H;
    private int W;
    private int[] buffer;
    private int[] bgBuffer;

    public Renderer(Box box) {
        H = box.H;
        W = box.W;
        buffer = box.getWindow().getDataBufferInt();
    }

    public void setBackground(BxImage bg) {
        bgBuffer = bg.getBuffer();
    }

    private void setPixel(int x, int y, int color, int alpha) {
        if (x < 0 || x >= W || y < 0 || y >= H || color == alpha) return;
        buffer[x + y * W] = color;
    }

    public void renderBxObject(BxObject go, int frame) {
        renderImage(go.getImage(frame), go.getX() - go.size / 2, go.getY() - go.size / 2,-1);
    }

    public void renderImage(BxImage image, int sx, int sy, int alpha) {
        if (alpha == AUTO_ALPHA) {
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
            if (color == COLOR_BG) {
                buffer[i] = bgBuffer[i];
            } else {
                buffer[i] = color;
            }
        }
    }
}


