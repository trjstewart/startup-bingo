package co.startupbingo.startupbingo.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;

public class SquareGameTileLayout extends RelativeLayout {

    public SquareGameTileLayout(Context context) {
        super(context);
    }

    public SquareGameTileLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SquareGameTileLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, widthMeasureSpec);
    }
}
