package org.sushobh.signview;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sushobh on 12-02-2018.
 */

public class Stretch {
    List<PressedPoint> points;

    public Stretch(List<PressedPoint> points) {
        this.points = new ArrayList<>();
        this.points.addAll(points);
    }

    public List<PressedPoint> getPoints() {
        return points;
    }

    public void setPoints(List<PressedPoint> points) {
        this.points = points;
    }
}
