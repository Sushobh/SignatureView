package org.sushobh.signview;

/**
 * Created by Sushobh on 12-02-2018.
 */

public class Option {

    String title;
    int resourceId;
    int width = -1;
    int height = -1;
    int rightPadding = -1;
    int leftPadding = -1;
    int topPadding = -1;
    int bottomPadding = -1;


    //These two variables are only useful for checking if this option is withing the range of click....
    int leftStartingPoint;
    int topStartingPoint;

    public int getLeftStartingPoint() {
        return leftStartingPoint;
    }

    public void setLeftStartingPoint(int leftStartingPoint) {
        this.leftStartingPoint = leftStartingPoint;
    }

    public int getTopStartingPoint() {
        return topStartingPoint;
    }

    public void setTopStartingPoint(int topStartingPoint) {
        this.topStartingPoint = topStartingPoint;
    }

    public int getRightPadding() {
        return rightPadding;
    }

    public void setRightPadding(int rightPadding) {
        this.rightPadding = rightPadding;
    }

    public int getLeftPadding() {
        return leftPadding;
    }

    public void setLeftPadding(int leftPadding) {
        this.leftPadding = leftPadding;
    }

    public int getTopPadding() {
        return topPadding;
    }

    public void setTopPadding(int topPadding) {
        this.topPadding = topPadding;
    }

    public int getBottomPadding() {
        return bottomPadding;
    }

    public void setBottomPadding(int bottomPadding) {
        this.bottomPadding = bottomPadding;
    }

    public Option(String title, int resourceId) {
        this.title = title;
        this.resourceId = resourceId;
    }


    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getResourceId() {
        return resourceId;
    }

    public void setResourceId(int resourceId) {
        this.resourceId = resourceId;
    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean hasBeenClicked(float touchX, float touchY){

       if(touchX>=leftStartingPoint&&touchY>=topStartingPoint){
           if(touchX<=leftStartingPoint+width&&touchY<=topStartingPoint+height){
               return true;
           }
       }

       return false;
    }
}
