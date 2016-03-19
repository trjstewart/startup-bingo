package co.startupbingo.startupbingo.ui;

import android.content.Context;
import android.widget.RelativeLayout;

public class SquareGameTileLayout extends RelativeLayout {

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, widthMeasureSpec);
    }

    public SquareGameTileLayout(Context context) {
        super(context);
    }
}
