package org.sushobh.signatureview.SignatureView;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.view.MotionEventCompat;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import org.sushobh.signatureview.R;

import java.util.ArrayList;
import java.util.List;


import static android.view.MotionEvent.INVALID_POINTER_ID;

/**
 * Created by Sushobh on 05-02-2018.
 */

public class SignatureView extends View  {


    ArrayList<PressedPoint> pressedPoints = new ArrayList<>();
    List<Stretch> stretches = new ArrayList<>();
    List<Option> options = new ArrayList<>();
    private float mLastTouchX;
    private float mLastTouchY;
    private int colorOfPaint;
    private int strokeWidth = 2;
    private int borderPaintStrokeWidth = 5;
    private boolean antiAliasRequired = true;
    private int mActivePointerId = INVALID_POINTER_ID;
    private boolean borderRequired = true;
    private int borderInnerPadding = 20;
    private int optionsBarLeftPadding = 20;
    private int optionsBarRightPadding = 20;
    private int optionsBarTopPadding = 10;
    private int optionBitmapHeight = 25;
    private int optionBitmapWidth = 25;
    private int optionPadding = 5;
    private int bitmapTopPadding = 10;
    private int borderTopPadding = optionBitmapHeight + bitmapTopPadding + 10;
    private int heightOfOptionBarSoFar = borderTopPadding;
    private int borderBottomPadding = 20;
    public Bitmap existingPicture;
    private OptionClickListener optionClickListener;


    public void setOptionClickListener(OptionClickListener optionClickListener) {
        this.optionClickListener = optionClickListener;
    }

    public Bitmap getExistingPicture() {
        return existingPicture;
    }

    public void setExistingPicture(Bitmap existingPicture) {
        this.existingPicture = existingPicture;
    }

    public boolean isBorderRequired() {
        return borderRequired;
    }

    public void setBorderRequired(boolean borderRequired) {
        this.borderRequired = borderRequired;
    }

    public boolean isAntiAliasRequired() {
        return antiAliasRequired;
    }

    public void setAntiAliasRequired(boolean antiAliasRequired) {
        this.antiAliasRequired = antiAliasRequired;
    }

    public int getColorOfPaint() {
        return colorOfPaint;
    }

    public void setColorOfPaint(int colorOfPaint) {
        this.colorOfPaint = colorOfPaint;
    }

    public int getStrokeWidth() {
        return strokeWidth;
    }

    public void setStrokeWidth(int strokeWidth) {
        this.strokeWidth = strokeWidth;
    }

    public SignatureView(Context context) {
        super(context);
        init();
    }



    public SignatureView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public SignatureView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public SignatureView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }


    private void init() {
        setColorOfPaint(Color.RED);
        addOption(new Option("Clear canvas", R.drawable.ic_clear_bitmap));
    }


    public void addOption(Option option){
        options.add(option);
        if(option.getWidth() == -1){
            option.setWidth(optionBitmapWidth);
        }
        if(option.getHeight() == -1){
            option.setHeight(optionBitmapHeight);
        }
        if(option.getRightPadding() == -1){
          option.setRightPadding(optionPadding);
        }

        if(option.getLeftPadding() == -1){
            option.setLeftPadding(optionPadding);
        }
        if(option.getTopPadding() == -1){
            option.setTopPadding(optionPadding);
        }
        if(option.getBottomPadding() == -1){
            option.setBottomPadding(optionPadding);
        }

        invalidate();
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawOptions(canvas);
        drawExistingBitmapIfPresent(canvas);
        if(isBorderRequired()){
            drawBorder(canvas);
        }
        drawPreviousStretches(canvas);
        drawPointList(pressedPoints,canvas);
    }

    private void drawExistingBitmapIfPresent(Canvas canvas) {
        if(existingPicture != null){
            canvas.drawBitmap(existingPicture,borderInnerPadding,heightOfOptionBarSoFar,getPaint());
        }
    }

    private void drawOptions(Canvas canvas) {
        int leftStartingPoint = optionsBarLeftPadding;
        int topStartingPoint = optionsBarTopPadding;
        int heighestBitmap = 0;
        for(Option option : options){
            Bitmap icon = BitmapFactory.decodeResource(getResources(),
                    option.getResourceId());
            icon = getResizedBitmap(icon,optionBitmapWidth,optionBitmapHeight);
            option.setLeftStartingPoint(leftStartingPoint);
            option.setTopStartingPoint(topStartingPoint);
            if(option.getHeight()>=heighestBitmap){
                heighestBitmap = option.getHeight();
            }
            canvas.drawBitmap(icon,leftStartingPoint,topStartingPoint,getPaint());
            leftStartingPoint = leftStartingPoint + optionsBarLeftPadding+option.getLeftPadding()+option.getWidth()+option.getRightPadding();
            if(getWidth()-leftStartingPoint<=optionsBarRightPadding){
                topStartingPoint = topStartingPoint + heighestBitmap+optionsBarTopPadding;
                leftStartingPoint = optionsBarLeftPadding;
            }
        }
        heightOfOptionBarSoFar = topStartingPoint + heighestBitmap+optionsBarTopPadding;
    }



    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        //The array of pointers is made up of every finger currently touching the screen
        final int action = MotionEventCompat.getActionMasked(ev);
        switch (action) {
            case MotionEvent.ACTION_DOWN: {
                final int pointerIndex = MotionEventCompat.getActionIndex(ev);
                final float x = MotionEventCompat.getX(ev, pointerIndex);
                final float y = MotionEventCompat.getY(ev, pointerIndex);
                // Remember where we started (for dragging)
                mLastTouchX = x;
                mLastTouchY = y;
                // Save the ID of this pointer (for dragging)
                mActivePointerId = MotionEventCompat.getPointerId(ev, 0);
                onTouchedAtPoint(mLastTouchX,mLastTouchY);

                break;
            }
            case MotionEvent.ACTION_MOVE: {
                // Find the index of the active pointer and fetch its position
                final int pointerIndex =
                        MotionEventCompat.findPointerIndex(ev, mActivePointerId);
                final float x = MotionEventCompat.getX(ev, pointerIndex);
                final float y = MotionEventCompat.getY(ev, pointerIndex);
                onActionMoveTo(x,y);
                // Calculate the distance moved
                final float dx = x - mLastTouchX;
                final float dy = y - mLastTouchY;
                mLastTouchX = x;
                mLastTouchY = y;
                break;
            }
            case MotionEvent.ACTION_UP: {
                onActionPointerUp();
                break;
            }
            case MotionEvent.ACTION_CANCEL: {
                mActivePointerId = INVALID_POINTER_ID;
                break;
            }
            case MotionEvent.ACTION_POINTER_UP: {
                final int pointerIndex = MotionEventCompat.getActionIndex(ev);
                final int pointerId = MotionEventCompat.getPointerId(ev, pointerIndex);
                if (pointerId == mActivePointerId) {
                    // This was our active pointer going up. Choose a new
                    // active pointer and adjust accordingly.
                    final int newPointerIndex = pointerIndex == 0 ? 1 : 0;
                    mLastTouchX = MotionEventCompat.getX(ev, newPointerIndex);
                    mLastTouchY = MotionEventCompat.getY(ev, newPointerIndex);
                    mActivePointerId = MotionEventCompat.getPointerId(ev, newPointerIndex);
                }
                break;
            }
        }
        return true;
    }

    private void onTouchedAtPoint(float mLastTouchX, float mLastTouchY) {
        for(Option option : options){
            if(option.hasBeenClicked(mLastTouchX,mLastTouchY)){
                onClickedOnOption(option);
            }
        }
    }

    public void onClickedOnOption(Option option) {
        if(option.getResourceId() == R.drawable.ic_clear_bitmap){
            clearSignature();
        }
        else {
            if(optionClickListener != null){
                optionClickListener.clickedOnOption(option);
            }
        }
    }


    public void clearSignature(){
        pressedPoints.clear();
        stretches.clear();
        existingPicture = null;
        invalidate();
    }


    private void onActionPointerUp() {
        mActivePointerId = INVALID_POINTER_ID;
        stretches.add(new Stretch(pressedPoints));
        pressedPoints.clear();
        invalidate();
    }

    private void onActionMoveTo(float x, float y) {

        float absXDistanceFromBorder = Math.abs(getWidth()-x);
        float absYDistnanceFromBorder = Math.abs(getHeight()-y);
        if(absXDistanceFromBorder<=borderInnerPadding){
            return;
        }

        if(x<=borderInnerPadding){
            return;
        }

        if(absYDistnanceFromBorder<=borderBottomPadding){
            return;
        }

        if(y<=heightOfOptionBarSoFar){
            return;
        }
        pressedPoints.add(new PressedPoint(x,y));
        invalidate();
    }


    private void drawPointList(List<PressedPoint> points,Canvas canvas) {
        Paint paint = getPaint();
        if(points.size()>0){
            PressedPoint prevPoint = points.get(0);
            for(int i = 1;i<points.size();i++){
                PressedPoint currPoint = points.get(i);
                canvas.drawLine(prevPoint.getX(),prevPoint.getY(),currPoint.getX(),currPoint.getY(),paint);
                prevPoint = currPoint;
            }
        }
    }

    private void drawPreviousStretches(Canvas canvas) {
        for(Stretch stretch : stretches){
            drawPointList(stretch.getPoints(),canvas);
        }
    }


    public Paint getPaint(){
        Paint paint = new Paint();
        paint.setColor(colorOfPaint);
        paint.setStrokeWidth(strokeWidth);
        paint.setAntiAlias(antiAliasRequired);
        return paint;
    }

    public Paint getBorderPaint(){
        Paint paint = new Paint();
        paint.setColor(Color.BLACK);
        paint.setStrokeWidth(borderPaintStrokeWidth);
        paint.setAntiAlias(antiAliasRequired);
        return paint;
    }

    public void drawBorder(Canvas canvas){
        Paint borderPaint = getBorderPaint();
        canvas.drawLine(0,0,getWidth(),0,borderPaint);
        canvas.drawLine(0,0,0,getHeight(),borderPaint);
        canvas.drawLine(0,getHeight(),getWidth(),getHeight(),borderPaint);
        canvas.drawLine(getWidth(),0,getWidth(),getHeight(),borderPaint);
    }


    public Bitmap getResizedBitmap(Bitmap bm, int newWidth, int newHeight) {
        int width = bm.getWidth();
        int height = bm.getHeight();
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // CREATE A MATRIX FOR THE MANIPULATION
        Matrix matrix = new Matrix();
        // RESIZE THE BIT MAP
        matrix.postScale(scaleWidth, scaleHeight);

        // "RECREATE" THE NEW BITMAP
        Bitmap resizedBitmap = Bitmap.createBitmap(
                bm, 0, 0, width, height, matrix, false);
        bm.recycle();
        return resizedBitmap;
    }


    public Bitmap getSignatureBitmap(){
        Bitmap bitmap = getBitmapFromView(this);
        return Bitmap.createBitmap(bitmap,borderInnerPadding,heightOfOptionBarSoFar,getWidth()-borderInnerPadding,getHeight()-heightOfOptionBarSoFar-borderInnerPadding);
    }

    private static Bitmap getBitmapFromView(View view) {
        Bitmap returnedBitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(returnedBitmap);
        Drawable bgDrawable = view.getBackground();
        if (bgDrawable!=null)
            bgDrawable.draw(canvas);
        else
            canvas.drawColor(Color.WHITE);
        view.draw(canvas);
        return returnedBitmap;
    }


}
