package com.wenjian.commonskill.widget.recycler;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;

import com.wenjian.commonskill.R;

import java.util.ArrayList;
import java.util.List;

public class RecyclerView extends ViewGroup {

    private static final String TAG = "david";
    Recycler recycler;
    private Adapter adapter;
    //y偏移量      内容偏移量
    private int scrollY;

    //当前显示的View
    private List<View> viewList;
    //当前滑动的y值
    private int currentY;
    //行数
    private int rowCount;
    //初始化
    private boolean needRelayout;
    //    当前reclerView的宽度
    private int width;

    private int height;

    private int[] heights;
    //view的弟一行  是占内容的几行
    private int firstRow;
    //最小滑动距离
    private int touchSlop;
    public RecyclerView(Context context) {
        super(context);
    }

    public RecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        this.viewList = new ArrayList<>();
        this.needRelayout = true;
        final ViewConfiguration configuration = ViewConfiguration.get(context);
//    点击    28 -40  滑动
        this.touchSlop = configuration.getScaledTouchSlop();

    }


    public RecyclerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setAdapter(Adapter adapter) {
        this.adapter = adapter;
        if (adapter != null) {
            recycler = new Recycler(adapter.getViewTypeCount());
        }
        scrollY = 0;
        firstRow = 0;
        needRelayout = true;
        requestLayout();
    }

    //拦截 滑动事件  预处理 事件的过程
    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        boolean intercept = false;
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN: {
                currentY = (int) event.getRawY();
                break;
            }
            case MotionEvent.ACTION_MOVE: {
                int y2 = Math.abs(currentY - (int) event.getRawY());
                if (y2 > touchSlop) {
                    intercept = true;
                }
                break;
            }
        }
        return intercept;
    }

    @Override
    public void removeView(View view) {
        super.removeView(view);
        int typeView = (int) view.getTag(R.id.tag_type_view);
        recycler.addRecycledView(view, typeView);
    }

    //onLayout  1     n
    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        Log.i(TAG, "onLayout: ");
        if (needRelayout || changed) {
            needRelayout = false;
            viewList.clear();
            removeAllViews();
            if (adapter != null) {
//                    摆放子控件
                width = r - l;
                height = b - t;
                int left, top = 0, right, bottom;
//                第一行不是从0开始
                top = -scrollY;
//                rowCount  内容的item数量 1000           height 当前控件的高度
                for (int i = 0; i < rowCount && top < height; i++) {
                    bottom = top + heights[i];
                    //生成View
                    View view = makeAndStep(i, 0, top, width, bottom);
//                    计算View l  r   t   b
                    viewList.add(view);
                    top = bottom;
                }
            }
        }

    }

    private View makeAndStep(int row, int left, int top, int right, int bottom) {
//        实例化一个有宽度  高度的View
        View view = obtainView(row, right - left, bottom - top);
//        设置位置
        view.layout(left, top, right, bottom);
        return view;
    }

    private View obtainView(int row, int width, int height) {
        int itemType = adapter.getItemViewType(row);
        //根据这个类型 返回相应View  （布局）
//        初始化的时候 取不到
        View reclyView = recycler.getRecyclerView(itemType);
        View view = adapter.getView(row, reclyView, this);
        if (view == null) {
            throw new RuntimeException("convertView 不能为空");
        }
        //View不可能为空
//
        view.setTag(R.id.tag_type_view, itemType);
        view.setTag(R.id.tag_row, row);
//        测量
//VIEW 打tag   row    type
        view.measure(MeasureSpec.makeMeasureSpec(width, MeasureSpec.EXACTLY)
                , MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY));
        addView(view, 0);
        return view;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN: {


            }

            case MotionEvent.ACTION_MOVE: {
//                移动的距离   y方向
                int y2 = (int) event.getRawY();
                //   diffX>0    往左划
                int diffY = currentY - y2;
                scrollBy(0, diffY);
            }
        }
        return super.onTouchEvent(event);
    }

    //scrollBy
    @Override
    public void scrollBy(int x, int y) {
        scrollY += y;
//     scrollY取值   0 ---- 屏幕 的高度   0---无限大   2
//修正一下  内容的总高度 是他的边界值
        scrollY = scrollBounds(scrollY, firstRow, heights, height);
        if (scrollY > 0) {
            //            往上滑
            while (heights[firstRow] < scrollY) {
//              remove  item完全移出去了  应该 1  不应该2
                removeTop();


            }

            while (getFilledHeight() < height) {

                addBottom();
            }
//            scrollY=0
            scrollY -= heights[firstRow];
            firstRow++;

        } else if (scrollY < 0) {
            //            往下滑
            while (!viewList.isEmpty() && getFilledHeight() - heights[firstRow + viewList.size()] > height) {
                removeBottom();
            }

            while (0 > scrollY) {
                addTop();
                firstRow--;
                scrollY += heights[firstRow + 1];
            }
        }
//        重新对一个子控件进行重新layout
        repositionViews();

//        重绘
        awakenScrollBars();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        final int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        final int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int h = 0;
        if (adapter != null) {
            this.rowCount = adapter.getCount();
            heights = new int[rowCount];
            for (int i = 0; i < heights.length; i++) {
                heights[i] = adapter.getHeight(i);
            }
        }
        int tmpH = sumArray(heights, 0, heights.length);
        h = Math.min(heightSize, tmpH);
        setMeasuredDimension(widthSize, h);
    }

    private int sumArray(int array[], int firstIndex, int count) {
        int sum = 0;
        count += firstIndex;
        for (int i = firstIndex; i < count; i++) {
            sum += array[i];
        }
        return sum;
    }

    private void repositionViews() {
        int left, top, right, bottom, i;
        top = -scrollY;
        i = firstRow;
        for (View view : viewList) {
            bottom = top + heights[++i];
            view.layout(0, top, width, bottom);
            top = bottom;
        }
    }

    private void addTop() {
        addTopAndBottom(firstRow - 1, 0);
    }

    private void removeBottom() {
        removeTopOrBottom(viewList.size() - 1);
    }

    private void removeTopOrBottom(int position) {
        removeView(viewList.remove(position));
    }

    private int getFilledHeight() {
        return sumArray(heights, firstRow, viewList.size()) - scrollY;
    }

    private void addBottom() {
        final int size = viewList.size();
        addTopAndBottom(firstRow + size, size);
    }

    private void addTopAndBottom(int addRow, int size) {
        View view = obtainView(addRow, width, heights[addRow]);
        viewList.add(view);
    }

    private void removeTop() {
        removeView(viewList.get(0));
    }

    private int scrollBounds(int scrollY, int firstRow, int sizes[], int viewSize) {
        if (scrollY > 0) {
            //            往上滑
            scrollY = Math.min(scrollY, sumArray(sizes, firstRow, sizes.length - 1));
        } else {
            //            往下滑
            scrollY = Math.max(scrollY, 0);
        }
        return scrollY;
    }
}
