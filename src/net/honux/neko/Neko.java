package net.honux.neko;

public class Neko extends BxObject {



    public Neko(Box box, int delay, int scale) {
        super(box, delay, scale);
        setStatus(CatStatus.STAND.toString(), 0);
    }

    //Neko class test
    public static void main(String[] args) {
        Neko neko = new Neko(null, 3, 1);
        for (int i = 0; i < 30; i++) {
            System.out.println(neko.getImage(i));
        }
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
        setStatusImages(CatStatus.CATCH_R.toString(), 19, 20, 19, 20);
    }

    @Override
    public void update(int frame) {
        x += dx;
        y += dy;

        if (!box.getCoins().isEmpty() && checkStatus(CatStatus.STAND)) {
            runForCoin();
            setStatus(CatStatus.TOP.toString(), frame);
            return;
        }

        if (checkStatus(CatStatus.TOP) && catchCoin()) {
            setStatus(CatStatus.CATCH_R.toString(), frame);
            dx = 0;
            dy = 0;
            return;
        }

        if (checkStatus(CatStatus.CATCH_R) && checkFrame(frame, getStatusImageSize(CatStatus.CATCH_R.toString()))) {
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

        //System.out.printf("Neko: %s\n", status);
    }

    boolean catchCoin() {
        if (box.getCoins().isEmpty()) return false;
        var coin = box.getCoins().get(0);

        return (Math.abs(x - coin.x) < 5 && Math.abs(y - coin.y) < 5);
    }
    private void runForCoin() {
        Coin coin = box.getCoins().get(0);
        if (coin == null) return;
        v = 0.7f;
        double base = Math.sqrt(Math.pow(coin.x - x, 2) + Math.pow(coin.y - y, 2));
        dx = v * (coin.x - x) / base;
        dy = v * (coin.y - y) / base;
    }

    private boolean checkFrame(int frame, int time) {
        return frame > this.getLastFrame() + delay * time;
    }

    private boolean checkStatus(CatStatus status) {
        return this.status == status.toString();
    }
}
