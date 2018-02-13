package org.sushobh.signatureview.SignatureView;

/**
 * Created by Sushobh on 12-02-2018.
 */

public class PressedPoint {
    float x;
    float y;

    public PressedPoint(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }
}
