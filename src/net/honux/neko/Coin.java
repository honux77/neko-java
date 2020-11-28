package net.honux.neko;

public class Coin extends BxObject {

    private final String NORMAL = "NORMAL";
    private boolean show = true;

    public Coin(Box box, int x, int y, int scale) {
        super(box, box.DELAY, scale);
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
        if (collideWith(box.getNeko())) {
            System.out.printf("고양이와 충돌: %.2f %.2f\n", x, y);
        }
    }
}
