package in.webx.scorpion.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public class SignatureView extends View {

    private static final float STROKE_WIDTH = 5f;
    private static final float HALF_STROKE_WIDTH = STROKE_WIDTH / 2;
    private Paint paint = new Paint();
    private Path path = new Path();

    private float lastTouchX;
    private float lastTouchY;
    private final RectF dirtyRect = new RectF();

    public SignatureView(Context context, AttributeSet attrs) {
        super(context, attrs);
        paint.setAntiAlias(true);
        paint.setColor(Color.BLACK);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeJoin(Paint.Join.ROUND);
        paint.setStrokeWidth(STROKE_WIDTH);
    }

    public void clear() {
        path.reset();
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawPath(path, paint);
    }

    public Bitmap getBitmap() {
        this.setDrawingCacheEnabled(true);
        this.buildDrawingCache();
        Bitmap bmp = Bitmap.createBitmap(this.getDrawingCache());
        this.setDrawingCacheEnabled(false);
        return bmp;
    }

    @SuppressLint("ClickableViewAccessibility") @Override
    public boolean onTouchEvent(MotionEvent event) {
        float eventX = event.getX();
        float eventY = event.getY();
        // mGetSign.setEnabled(true);

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN :
                path.moveTo(eventX, eventY);
                lastTouchX = eventX;
                lastTouchY = eventY;
                return true;

            case MotionEvent.ACTION_MOVE :

            case MotionEvent.ACTION_UP :

                resetDirtyRect(eventX, eventY);
                int historySize = event.getHistorySize();
                for (int i = 0; i < historySize; i++) {
                    float historicalX = event.getHistoricalX(i);
                    float historicalY = event.getHistoricalY(i);
                    expandDirtyRect(historicalX, historicalY);
                    path.lineTo(historicalX, historicalY);
                }
                path.lineTo(eventX, eventY);
                break;

            default :
                debug("Ignored touch event: " + event.toString());
                return false;
        }

        invalidate((int) (dirtyRect.left - HALF_STROKE_WIDTH),
                (int) (dirtyRect.top - HALF_STROKE_WIDTH),
                (int) (dirtyRect.right + HALF_STROKE_WIDTH),
                (int) (dirtyRect.bottom + HALF_STROKE_WIDTH));

        lastTouchX = eventX;
        lastTouchY = eventY;

        return true;
    }

    private void debug(String string) {
    }

    private void expandDirtyRect(float historicalX, float historicalY) {
        if (historicalX < dirtyRect.left) {
            dirtyRect.left = historicalX;
        } else if (historicalX > dirtyRect.right) {
            dirtyRect.right = historicalX;
        }

        if (historicalY < dirtyRect.top) {
            dirtyRect.top = historicalY;
        } else if (historicalY > dirtyRect.bottom) {
            dirtyRect.bottom = historicalY;
        }
    }

    private void resetDirtyRect(float eventX, float eventY) {
        dirtyRect.left = Math.min(lastTouchX, eventX);
        dirtyRect.right = Math.max(lastTouchX, eventX);
        dirtyRect.top = Math.min(lastTouchY, eventY);
        dirtyRect.bottom = Math.max(lastTouchY, eventY);
    }
/*    private Path mPath;
    private Paint mPaint;
    private Paint bgPaint = new Paint(Color.TRANSPARENT);

    private Bitmap mBitmap;
    private Canvas mCanvas;

    private float curX, curY;

    private static final int TOUCH_TOLERANCE = 4;
    private static final int STROKE_WIDTH = 4;

    public SignatureView(Context context) {
        super(context);
        init();
    }
    public SignatureView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }
    public SignatureView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        setFocusable(true);
        mPath = new Path();
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setColor(Color.WHITE);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(STROKE_WIDTH);
    }

    public void setSigColor(int color) {
        mPaint.setColor(color);
    }
    public void setSigColor(int a, int red, int green, int blue) {
        mPaint.setARGB(a, red, green, blue);
    }

    public boolean clearSignature() {
        if (mBitmap != null)
            createFakeMotionEvents();
        if (mCanvas != null) {
            mCanvas.drawColor(Color.BLACK);
            mCanvas.drawPaint(bgPaint);
            mPath.reset();
            invalidate();
        }
        else {
            return false;
        }
        return true;
    }

    public Bitmap getImage() {
        return this.mBitmap;
    }
    public void setImage(Bitmap bitmap) {
        this.mBitmap = bitmap;
        this.invalidate();
    }

    @Override
    protected void onSizeChanged(int width, int height, int oldWidth, int oldHeight) {
        int bitmapWidth = mBitmap != null ? mBitmap.getWidth() : 0;
        int bitmapHeight = mBitmap != null ? mBitmap.getWidth() : 0;
        if (bitmapWidth >= width && bitmapHeight >= height)
            return;
        if (bitmapWidth < width)
            bitmapWidth = width;
        if (bitmapHeight < height)
            bitmapHeight = height;
        Bitmap newBitmap = Bitmap.createBitmap(bitmapWidth, bitmapHeight, Bitmap.Config.ARGB_8888);
        Canvas newCanvas = new Canvas();
        newCanvas.setBitmap(newBitmap);
        if (mBitmap != null)
            newCanvas.drawBitmap(mBitmap, 0, 0, null);
        mBitmap = newBitmap;
        mCanvas = newCanvas;
    }

    private void createFakeMotionEvents() {
        MotionEvent downEvent = MotionEvent.obtain(SystemClock.uptimeMillis(),SystemClock.uptimeMillis()+100, MotionEvent.ACTION_DOWN, 1f, 1f ,0);
        MotionEvent upEvent = MotionEvent.obtain(SystemClock.uptimeMillis(),SystemClock.uptimeMillis()+100, MotionEvent.ACTION_UP, 1f, 1f ,0);
        onTouchEvent(downEvent);
        onTouchEvent(upEvent);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawColor(Color.BLACK);
        canvas.drawBitmap(mBitmap, 0, 0, mPaint);
        canvas.drawPath(mPath, mPaint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                touchDown(x, y);
                break;
            case MotionEvent.ACTION_MOVE:
                touchMove(x, y);
                break;
            case MotionEvent.ACTION_UP:
                touchUp();
                break;
        }
        invalidate();
        return true;
    }

    *//**----------------------------------------------------------
     * Private methods
     **---------------------------------------------------------*//*

    private void touchDown(float x, float y) {
        mPath.reset();
        mPath.moveTo(x, y);
        curX = x;
        curY = y;
    }

    private void touchMove(float x, float y) {
        float dx = Math.abs(x - curX);
        float dy = Math.abs(y - curY);
        if (dx >= TOUCH_TOLERANCE || dy >= TOUCH_TOLERANCE) {
            mPath.quadTo(curX, curY, (x + curX)/2, (y + curY)/2);
            curX = x;
            curY = y;
        }
    }

    private void touchUp() {
        mPath.lineTo(curX, curY);
        if (mCanvas == null) {
            mCanvas = new Canvas();
            mCanvas.setBitmap(mBitmap);
        }
        mCanvas.drawPath(mPath, mPaint);
        mPath.reset();
    }*/
}
