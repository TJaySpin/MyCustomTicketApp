package com.example.mytaobaounion.UI.Custom;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.mytaobaounion.R;
import com.example.mytaobaounion.Utils.LogUtils;

import java.util.ArrayList;
import java.util.List;

public class TextFlowLayout extends ViewGroup {
    private List<String> mTextList=new ArrayList<>();

    //内部的viewLine用来储存每行的子View，而viewList储存每行viewLine，即所有子View
    private List<List<View>> viewList=new ArrayList<>();

    private onFlowTextClickListener flowTextClickListener;

    //每个item（词条TextView）的边距，在后面测量和布局中有重要作用；此外声明了自定义属性，方便在xml中直接设置和修改
    private static final int DEFAULT_SPACE=10;
    private float itemHorizonSpace=DEFAULT_SPACE;
    private float itemVeriticalSpace=DEFAULT_SPACE;


    //分别是所有子view允许的宽度，允许的高度和每行的高度，测量时使用
    private int admitWidth;
    private int admitHeight;
    private int lineHeight;


    //构造方法统一指向三参数，这是为了方便拿到自定义属性
    public TextFlowLayout(Context context) {
        this(context,null);
    }

    public TextFlowLayout(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public TextFlowLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.TextFlowStyle);
        itemHorizonSpace = typedArray.getDimension(R.styleable.TextFlowStyle_horizontal_space, DEFAULT_SPACE);
        itemVeriticalSpace = typedArray.getDimension(R.styleable.TextFlowStyle_vertical_space, DEFAULT_SPACE);
        typedArray.recycle();
    }

    public int getContentSize(){
        return mTextList.size();
    }

    public float getItemHorizonSpace() {
        return itemHorizonSpace;
    }

    public void setItemHorizonSpace(int itemHorizonSpace) {
        this.itemHorizonSpace = itemHorizonSpace;
    }

    public float getItemVeriticalSpace() {
        return itemVeriticalSpace;
    }

    public void setItemVeriticalSpace(int itemVeriticalSpace) {
        this.itemVeriticalSpace = itemVeriticalSpace;
    }


    //获得history和recommend的词条列表，就可以显示在自定义的TextFlowLayout上了
    public void setTextList(List<String> textList){
        removeAllViews();
        mTextList=textList;
        for(String text:mTextList){
            TextView textView = (TextView) LayoutInflater.from(getContext()).inflate(R.layout.textview_flowtext, this, false);     //之所以是false，是因为创建TextView之后要将textList的内容setText进去
            textView.setText(text);

            //对每个子View设置点击事件监听，暴露给View层，点击后进行搜索
            textView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(flowTextClickListener!=null){
                        flowTextClickListener.onFlowTextClick(textView.getText().toString());
                    }
                }
            });

            //设置不绑定false，则调用root.addView添加进去（root就是我们自定义的TextFlowLayout）
            addView(textView);
        }
    }


    /**
     * 测量前清空所有测量数据，包括每一行的测量数据
     * 先测量所有可见子View的宽度，如果总宽度超过adminWidth，代表这行已经测量完；
     * 上一步结束后，将viewLine添加进viewList，并创建新的viewLine即新的一行，然后把那个没加进去的子View添加进viewLine；
     * 由于每个子View样式一致（textView_flowText布局），因此行高lineHeight只需要任意一个子View去拿就行；
     * 可允许高度adminHeight就等于行数（即viewList的size）*真正的行高（即lineHeight+间隔）；
     * 最后通过setMeasuredDimension保存测量结果
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int childCount = getChildCount();
        if(childCount ==0){
            return;
        }

        admitWidth = MeasureSpec.getSize(widthMeasureSpec) - getPaddingLeft() - getPaddingRight();


        //由于会多次测量，因此必须保证每次测量都清空所有数据
        viewList.clear();
        //清空每行的数据
        List<View> viewLine=null;


        for(int i=0;i<childCount;i++){
            View childView = getChildAt(i);
            if(childView.getVisibility()!=VISIBLE){
                continue;
            }
            measureChild(childView,widthMeasureSpec,heightMeasureSpec);

            if(viewLine==null){
                viewLine=createViewLine(childView);
            }
            else{
                if(canAdd(childView,viewLine)){
                    viewLine.add(childView);
                }
                else{
                    viewLine=createViewLine(childView);
                }
            }
        }


        lineHeight = getChildAt(0).getMeasuredHeight();
        admitHeight = (int) (viewList.size()*(lineHeight +itemVeriticalSpace)+0.5f);                  //+0.5是为了强转int时实现四舍五入

        setMeasuredDimension(admitWidth,admitHeight);
    }


    //简单的封装，只有一行为空或者当前行装不下时调用
    private List<View> createViewLine(View childView) {
        List<View> viewLine=new ArrayList<>();
        viewLine.add(childView);
        viewList.add(viewLine);
        return viewLine;
    }


    //通过判断宽度是否超过adminWidth来判断是否可以添加
    private boolean canAdd(View childView, List<View> viewLine) {
        //注意这里是float
        float totalWidth = childView.getMeasuredWidth();
        for (View view : viewLine) {
            totalWidth += view.getMeasuredWidth();
        }
        totalWidth += itemHorizonSpace * (viewLine.size() + 1);
        return totalWidth <= admitWidth;
    }



    //布局就是设置每个子View的左上右下四个参数
    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int topPos= (int) itemVeriticalSpace;
        for(List<View> viewLine:viewList){
            int leftPos= (int) itemHorizonSpace;
            for(View view:viewLine){
                view.layout(leftPos,topPos,leftPos+view.getMeasuredWidth(),topPos+view.getMeasuredHeight());
                leftPos+=view.getMeasuredWidth()+itemHorizonSpace;
            }
            topPos+=lineHeight+itemVeriticalSpace;
        }
    }



    public interface onFlowTextClickListener{
        void onFlowTextClick(String text);
    }

    public void setFlowTextClickListener(onFlowTextClickListener flowTextClickListener) {
        this.flowTextClickListener = flowTextClickListener;
    }
}
