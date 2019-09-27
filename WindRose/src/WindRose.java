import processing.core.PApplet;
import processing.core.PConstants;

import java.util.ArrayList;

public class WindRose {
    private ArrayList<WindVector> windReadings;

    public WindRose(ArrayList<WindVector> w) {
        windReadings = w;
    }

    public double[][] relativeFrequencies() {
        double[][] results = new double[17][8];
        int obs = windReadings.size();

        for(WindVector w : windReadings) {
            int n = w.simplifiedSpeed();
            int m = w.simplifiedDirection();
            if(n == 0) results[16][0]++;
            else {
                results[m][0]++;
                results[m][n]++;
            }
        }

        for(int i = 0; i < 17; i++) {
            double size = 1;
            for(int j = 0; j < 8; j++) {
                if(j == 0) {
                    size = results[i][j];
                    results[i][j] /= obs;
                } else results[i][j] /= size;
            }
        }

        return results;
    }

    public void draw(PApplet p, float x, float y, float r) {
        p.pushStyle();
        double[][] results = relativeFrequencies();
        double max = findMax(results);
        double maxValue = Math.ceil(10 * max) / 10;

        int interval = 2;
        int n = 1;

        for(int i = 4; i < 8; i++) {
            if((maxValue * 100) % i == 0) {
                interval = (int)(maxValue * 100 / i);
                n = i;
                break;
            }
        }

        p.stroke(180);
        p.noFill();
        p.ellipse(x, y, 2*r, 2*r);

        for(int i = 1; i < n + 1; i++) {
            p.ellipse(x, y, i * 2*r / n, i * 2*r / n);
            p.fill(140);
            p.textSize(r/10);
            p.text(interval*i + "%", x + 0.05f*r, y - (r/n)*i);
            p.noFill();
        }

        p.line(x - 1.2f*r, y, x + 1.2f*r, y);
        p.line(x, y - 1.2f*r, x, y + 1.2f*r);

        p.stroke(0);
        for(int i = 0; i < 16; i++) {
            double freq = results[i][0];
            float radiusTotal = (float)((freq / maxValue) * r);

            for(int j = 8; j > 1; j--) {
                p.pushStyle();
                float radius = (float)(sumPart(results[i], 1, j) * radiusTotal);
                setFill(p, j);

                p.arc(x, y, 2*radius, 2*radius, (float)(Math.PI * i / 8 - Math.PI / 2 - Math.PI / 18), (float)(Math.PI * i / 8 - Math.PI / 2 + Math.PI / 18), PConstants.PIE);
                p.popStyle();
            }
        }

        double[] values = {15.5, 10.8, 8.23, 5.14, 3.09, 1.54};

        p.fill(0);
        p.textSize(r/8);
        p.text("N",x - r/16, y - 1.2f*r - r/16);
        p.text("E",x + 1.2f*r + r/16, y + r/16);
        p.text("S",x - 10, y + 1.2f*r + r/16 + r/8);
        p.text("W",x - 1.2f*r - r/16 - r/8, y + r/16);

        p.textSize(r/12);
        p.text(">15.5 m/s", x + 2*r + r/8, y - r);
        for(double d : values) p.text(d + "", x + 2*r + r/8, y - r + (1 - (float)d/25f)*2*r);
        p.text("Calm: " + Math.round(results[16][0] * 100) + "%", x + 2*r + r/8, y + r);

        p.fill(255, 0, 0);
        p.rect(x + 2*r, y - r, r/16, 2*r);

        for(int i = 3; i < 9; i++) {
            setFill(p, 10 - i);
            float f = (float)(values[i - 3]);
            p.rect(x + 2*r, y - r + (1 - f/25f)*2*r, r/16, (f/25f)*2*r);
        }

        p.popStyle();
    }

    private void setFill(PApplet p, int j) {
        if(j == 2) p.fill(113, 48, 199);
        else if(j == 3) p.fill(0, 31, 232);
        else if(j == 4) p.fill(35, 171, 32);
        else if(j == 5) p.fill(0, 255, 0);
        else if(j == 6) p.fill(255, 255, 0);
        else if(j == 7) p.fill(255, 127, 0);
        else p.fill(255, 0, 0);
    }

    private double sumPart(double[] array, int start, int end) {
        double sum = 0;
        for(int i = start; i < end; i++) {
            sum += array[i];
        }

        return sum;
    }

    private double findMax(double[][] results) {
        double max = results[0][0];

        for(int i = 0; i < 16; i++) {
            double[] d = results[i];
            double dd = d[0];
            if(dd > max) max = dd;
        }

        return max;
    }
}