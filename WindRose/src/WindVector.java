public class WindVector {
    private double speed;
    private int direction;

    public WindVector(double m, int theta) {
        speed = m;
        direction = theta;
    }

    public int simplifiedDirection() {
        int n = (int)(Math.round(direction / 22.5));
        if(n < 16) return n;
        else return 0;
    }

    public int simplifiedSpeed() {
        if(speed == 0) return 0;
        else if(speed < 1.54) return 1;
        else if(speed < 3.09) return 2;
        else if(speed < 5.14) return 3;
        else if(speed < 8.23) return 4;
        else if(speed < 10.8) return 5;
        else if(speed < 15.5) return 6;
        else return 7;
    }
}