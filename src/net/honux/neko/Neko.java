package net.honux.neko;

public class Neko extends BxObject {

    private boolean isRunning = false;

    public Neko(Box box, int delay, int scale) {
        super(box, delay, scale);
        setStatus(CatStatus.STAND.toString(), 0);
    }

    @Override
    public void addStatusForImages() {
        for (var status : CatStatus.values()) {
            addStatus(status.toString());
        }
    }

    @Override
    public void addStatusImages() {
        setStatusImages(CatStatus.STAND.toString(), 25, 26);
        setStatusImages(CatStatus.SLEEPY.toString(), 27, 28);
        setStatusImages(CatStatus.SLEEP.toString(), 29, 30);
        setStatusImages(CatStatus.WAKE_UP.toString(), 31, 32);
        setStatusImages(CatStatus.TOP.toString(), 1, 2);
        setStatusImages(CatStatus.TOP_RIGHT.toString(), 3, 4);
        setStatusImages(CatStatus.RIGHT.toString(), 5, 6);
        setStatusImages(CatStatus.BOTTOM_RIGHT.toString(), 7, 8);
        setStatusImages(CatStatus.BOTTOM.toString(), 9, 10);
        setStatusImages(CatStatus.BOTTOM_LEFT.toString(), 11, 12);
        setStatusImages(CatStatus.LEFT.toString(), 13, 14);
        setStatusImages(CatStatus.TOP_LEFT.toString(), 15, 16);

        setStatusImages(CatStatus.CATCH_R.toString(), 19, 20, 19, 20);
        setStatusImages(CatStatus.CATCH_L.toString(), 23, 24, 23, 24);

    }

    @Override
    public void update(int frame) {
        x += dx;
        y += dy;

        if (!box.getCoins().isEmpty() && checkStatus(CatStatus.STAND)) {
            String direction = runForCoin();
            setStatus(direction, frame);
            return;
        }

        if (isRunning && catchCoin()) {
            isRunning = false;
            setStatus(changeCatchStatus(), frame);
            dx = 0;
            dy = 0;
            return;
        }

        if ((checkStatus(CatStatus.CATCH_R) || checkStatus(CatStatus.CATCH_L)) && checkFrame(frame, getStatusImageSize(CatStatus.CATCH_R.toString()))) {
            setStatus(CatStatus.WAKE_UP.toString(), frame);
            box.getCoins().remove(0);
        }

        if (!checkStatus(CatStatus.SLEEP) && !checkStatus(CatStatus.SLEEPY) && box.getCoins().isEmpty()) {
            setStatus(CatStatus.SLEEPY.toString(), frame);
            dx = 0;
            dy = 0;
        }

        if(checkStatus(CatStatus.SLEEP) && !box.getCoins().isEmpty()) {
            setStatus(CatStatus.WAKE_UP.toString(), frame);
        }

        if (checkStatus(CatStatus.SLEEPY) && checkFrame(frame, getStatusImageSize(CatStatus.SLEEPY.toString()))) {
            setStatus(CatStatus.SLEEP.toString(), frame);
            v = 0;
            dx = 0;
            dy = 0;
        }

        if (checkStatus(CatStatus.WAKE_UP) && checkFrame(frame, getStatusImageSize(CatStatus.WAKE_UP.toString()))) {
            setStatus(CatStatus.STAND.toString(), frame);
        }
    }

    private String changeCatchStatus() {
        if (status == CatStatus.TOP.toString() || status == CatStatus.TOP_RIGHT.toString() ||
                status == CatStatus.RIGHT.toString() || status == CatStatus.BOTTOM_RIGHT.toString()) {
            return CatStatus.CATCH_R.toString();
        }
        return CatStatus.CATCH_L.toString();
    }

    boolean catchCoin() {
        if (box.getCoins().isEmpty()) return false;
        var coin = box.getCoins().get(0);

        return (Math.abs(x - coin.x) < 5 && Math.abs(y - coin.y) < 5);
    }
    private String runForCoin() {
        Coin coin = box.getCoins().get(0);
        if (coin == null) return CatStatus.STAND.toString();

        isRunning = true;
        v = 0.7f;
        double base = Math.sqrt(Math.pow(coin.x - x, 2) + Math.pow(coin.y - y, 2));
        dx = v * (coin.x - x) / base;
        dy = v * (coin.y - y) / base;

        double deg = Math.atan2(coin.y - y, coin.x - x);
        final double p = Math.PI / 4;
        final double p2 = Math.PI / 8;
        if (deg > -p2 && deg <= p2) return CatStatus.RIGHT.toString();
        if (deg > p2 && deg <= p2 + p) return CatStatus.BOTTOM_RIGHT.toString();
        if (deg > p2 + p && deg <= p2 + 2 * p) return CatStatus.BOTTOM.toString();
        if (deg > p2 + 2 * p && deg <= p2 + 3 * p) return CatStatus.BOTTOM_LEFT.toString();
        if (deg < -p2 && deg >= -p2 - p) return CatStatus.TOP_RIGHT.toString();
        if (deg < -p2 - p && deg >= -p2 - 2 * p) return CatStatus.TOP.toString();
        if (deg < -p2 - 2 * p && deg >= -p2 - 3 * p) return CatStatus.TOP_LEFT.toString();
        return CatStatus.LEFT.toString();
    }

    private boolean checkFrame(int frame, int time) {
        return frame > this.getLastFrame() + delay * time;
    }

    private boolean checkStatus(CatStatus status) {
        return this.status == status.toString();
    }
}
