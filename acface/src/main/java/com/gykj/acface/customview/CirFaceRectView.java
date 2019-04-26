package com.gykj.acface.customview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Path;
import android.graphics.Region;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.gykj.acface.model.DrawInfo;
import com.gykj.acface.util.DrawHelper;
import com.gykj.acface.util.SystemUtil;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class CirFaceRectView extends FaceRectView {
    private  int heightSize;
    private  int widthSize;

    private CopyOnWriteArrayList<DrawInfo> faceRectList = new  CopyOnWriteArrayList<>();

    public CirFaceRectView(Context context) {
        this(context, null);
    }

    public CirFaceRectView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }
    public CirFaceRectView(Context context, AttributeSet attrs , int defsty) {
        super(context, attrs, defsty);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        heightSize = MeasureSpec.getSize(heightMeasureSpec);
        widthSize = MeasureSpec.getSize(widthMeasureSpec);
        //获取屏幕长宽比例，这样设置不会发生畸变，千万不要根据一个手机设定一个数
        //那样换一个手机就可能会出现显示的比例问题
        int screenWidth = SystemUtil.getScreenWidth(getContext());
        int screenHeight = SystemUtil.getScreenHeight(getContext());
        //可以理解为红色的背景盖住了大部分的区域，我们只能看到圆框里面的，如果还是按照原来的比例绘制surfaceview
        //需要把手机拿的很远才可以显示出整张脸，故而我用了一个比较取巧的办法就是，按比例缩小，试验了很多数后，感觉0.55
        //是最合适的比例
        double screenWidth1= 0.55*screenWidth;
        double screenHeight1= 0.55*screenHeight;
        //绘制的输入参数必须是整数型，做浮点型运算后为float型数据，故需要做取整操作
        setMeasuredDimension((int) screenWidth1, (int) screenHeight1);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        Path path = new Path();
        //设置裁剪的圆心，半径
        path.addCircle(heightSize / 2, heightSize / 2, heightSize, Path.Direction.CCW);
        //裁剪画布，并设置其填充方式
        canvas.clipPath(path, Region.Op.REPLACE);

        super.onDraw(canvas);
    }
}