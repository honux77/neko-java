package net.honux.neko;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public class Coin extends GameObject{

    private final String NORMAL = "NORMAL";
    private boolean show = true;
    public Coin(MainWindow w, int x, int y, int scale) {
        super(w, w.DELAY, scale);
        status=NORMAL;
        setPosition(x, y);
    }
    @Override
    public void addStatusForImages() {
        addStatus(NORMAL);
    }

    @Override
    public void addStatusImages() {
        setStatusImages(NORMAL, 1, 2, 3, 4);
    }

    @Override
    public void update(int frame) {
        if (collideWith(mainWindow.getNeko())) {
            System.out.printf("고양이와 충돌: %.2f %.2f\n", x, y);
            show = false;
        }
    }
}
