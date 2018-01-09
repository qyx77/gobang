package com.example.administrator.gobang;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by Administrator on 2018/1/6.
 */
public class GobangView extends View {

    private int              LineNumber=10;
    private int              mViewWidth;
    private float            mBlockWidth;
    private Paint            paint;
    private Bitmap           mWhitePiece;
    private Bitmap           mBlackPiece;
    private float            ratioOfPiece=3*1.0f/4;//棋子相对于格子的宽度缩放的比例
    private int              pieceWidth;
    private ArrayList<Point> mWhitePieceArray=new ArrayList<Point>();
    private ArrayList<Point> mBlackPieceArray=new ArrayList<Point>();
    private boolean          isWhite=true;//true代表现在下的是白棋，否则是黑旗
    private boolean          isWhiteWin;
    private boolean          isBlackWin;
    private boolean          isGameOver;
    private int              winNumber=5;//设置几个棋是赢，本文设置为5
    public GobangView(Context context) {
        super(context);
        init();
    }

    public GobangView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public GobangView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }
    private void init()
    {
        setBackgroundColor(0x44ff0000);
        paint=new Paint();
        paint.setColor(0x88000000);
        paint.setAntiAlias(true);
        paint.setDither(true);
        paint.setStyle(Paint.Style.STROKE);
        mWhitePiece= BitmapFactory.decodeResource(getResources(),R.drawable.stone_w2);
        mBlackPiece=BitmapFactory.decodeResource(getResources(),R.drawable.stone_b1);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthSize=MeasureSpec.getSize(widthMeasureSpec);
        int widthMode=MeasureSpec.getMode(widthMeasureSpec);

        int heightSize=MeasureSpec.getSize(heightMeasureSpec);
        int heightMode=MeasureSpec.getMode(heightMeasureSpec);
        int width=widthSize<heightSize?widthSize:heightSize;
        if(widthMode==MeasureSpec.UNSPECIFIED)
        {
            width=heightSize;
        }
       else if(heightMode==MeasureSpec.UNSPECIFIED)
        {
            width=widthSize;
        }
        setMeasuredDimension(width,width);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mViewWidth=w;
        mBlockWidth=w*1.0f/LineNumber;
        pieceWidth=(int)(mBlockWidth*ratioOfPiece);
        mWhitePiece=Bitmap.createScaledBitmap(mWhitePiece,pieceWidth,pieceWidth,false);
        mBlackPiece=Bitmap.createScaledBitmap(mBlackPiece,pieceWidth,pieceWidth,false);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawBoard(canvas);
        drawWhitePiece(canvas);
        drawBlackPiece(canvas);
        checkIsGameOver();
    }

    private void checkIsGameOver() {
        isWhiteWin=isFivePieceInLine(mWhitePieceArray);
        isBlackWin=isFivePieceInLine(mBlackPieceArray);
        if(isWhiteWin||isBlackWin)
        {
            isGameOver=true;
            String text=isWhiteWin?"白棋胜利":"黑旗胜利";
            Toast.makeText(getContext(),text,Toast.LENGTH_SHORT).show();
        }
    }

    private boolean isFivePieceInLine(ArrayList<Point> mPieceArray) {
        boolean isWin=false;
        for (Point point: mPieceArray) {
            int x=point.x;
            int y=point.y;


            isWin=isVerticalFivePiece(x,y,mPieceArray);
            if(isWin) return true;
            isWin=isLeftDiagonalFivePiece(x,y,mPieceArray);
            if(isWin) return true;
            isWin=isRightDiagonalFivePiece(x,y,mPieceArray);
            if(isWin) return true;
            isWin=isHorizontalFivePiece(x,y,mPieceArray);

        }
        return isWin;
    }

    private boolean isHorizontalFivePiece(int x, int y, ArrayList<Point> mPieceArray) {
        int count=1;
        for (int i=1; i<winNumber;i++) {
            if(mPieceArray.contains(new Point(x-i,y)))
            {
                count++;
            }
            else
            {
                break;
            }
        }
        for (int i=1; i<winNumber;i++) {
            if(mPieceArray.contains(new Point(x+i,y)))
            {
                count++;
            }
            else
            {
                break;
            }
        }
        if(count==winNumber){return true;}
        return false;

    }
    private boolean isVerticalFivePiece(int x, int y, ArrayList<Point> mPieceArray) {
        int count=1;
        for (int i=1; i<winNumber;i++) {
            if(mPieceArray.contains(new Point(x,y-i)))
            {
                count++;
            }
            else
            {
                break;
            }
        }
        for (int i=1; i<winNumber;i++) {
            if(mPieceArray.contains(new Point(x,y+i)))
            {
                count++;
            }
            else
            {
                break;
            }
        }
        if(count==winNumber){return true;}
        return false;
    }
    private boolean isLeftDiagonalFivePiece(int x, int y, ArrayList<Point> mPieceArray) {
        int count=1;
        for (int i=1; i<winNumber;i++) {
            if(mPieceArray.contains(new Point(x-i,y+i)))
            {
                count++;
            }
            else
            {
                break;
            }
        }
        for (int i=1; i<winNumber;i++) {
            if(mPieceArray.contains(new Point(x+i,y-i)))
            {
                count++;
            }
            else
            {
                break;
            }
        }
        if(count==winNumber){return true;}
        return false;
    }
    private boolean isRightDiagonalFivePiece(int x, int y, ArrayList<Point> mPieceArray) {
        int count=1;
        for (int i=1; i<winNumber;i++) {
            if(mPieceArray.contains(new Point(x-i,y-i)))
            {
                count++;
            }
            else
            {
                break;
            }
        }
        for (int i=1; i<winNumber;i++) {
            if(mPieceArray.contains(new Point(x+i,y+i)))
            {
                count++;
            }
            else
            {
                break;
            }
        }
        if(count==winNumber){return true;}
        return false;
    }

    private void drawWhitePiece(Canvas canvas) {
        for (Point point: mWhitePieceArray) {
            float x=(point.x+(1-ratioOfPiece)/2)*mBlockWidth;
            float y=(point.y+(1-ratioOfPiece)/2)*mBlockWidth;
            canvas.drawBitmap(mWhitePiece,x,y,null);
        }
    }
    private void drawBlackPiece(Canvas canvas) {
        for (Point point: mBlackPieceArray) {
            float x=(point.x+(1-ratioOfPiece)/2)*mBlockWidth;
            float y=(point.y+(1-ratioOfPiece)/2)*mBlockWidth;
            canvas.drawBitmap(mBlackPiece,x,y,null);
        }
    }

    private void drawBoard(Canvas canvas) {
        for(int i=0;i<LineNumber;i++)
        {
            float startX=mBlockWidth/2;
            float endX=mViewWidth-mBlockWidth/2;
            float y=(float)((0.5+i)*mBlockWidth);
            canvas.drawLine(startX,y,endX,y,paint);
        }
        for(int i=0;i<LineNumber;i++)
        {
            float startY=mBlockWidth/2;
            float endY=mViewWidth-mBlockWidth/2;
            float x=(float)((0.5+i)*mBlockWidth);
            canvas.drawLine(x,startY,x,endY,paint);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(isGameOver)
        {
            return false;
        }
        int action=event.getAction();
        if(action==MotionEvent.ACTION_DOWN)
        {
            return true;
        }

        if(action==MotionEvent.ACTION_UP)
        {
            float x=event.getX();
            float y=event.getY();
            Point point=getInvalidatePoint(x,y);
            //如果该点已经有棋子，就不处理该事件
            if(mWhitePieceArray.contains(point)||mBlackPieceArray.contains(point))
            {
                return false;
            }
            if(isWhite)
            {
                mWhitePieceArray.add(point);
            }
            else
            {
                mBlackPieceArray.add(point);
            }
            invalidate();
            isWhite=!isWhite;
        }
        return true;
    }
    private Point getInvalidatePoint(float x,float y)
    {
        int invalidateX=(int)(x/mBlockWidth);
        int invalidateY=(int)(y/mBlockWidth);
        return new Point(invalidateX,invalidateY);
    }
    public void clear()
    {
        mWhitePieceArray.clear();
        mBlackPieceArray.clear();
        isWhiteWin=false;
        isBlackWin=false;
        isGameOver=false;
        invalidate();
    }
    public void setmBlackPiecePhoto(Bitmap bitmap)
    {
        mBlackPiece=bitmap;
        invalidate();
    }
    public void setmWhitePiecePhoto(Bitmap bitmap)
    {
        mWhitePiece=bitmap;
        invalidate();
    }
}
