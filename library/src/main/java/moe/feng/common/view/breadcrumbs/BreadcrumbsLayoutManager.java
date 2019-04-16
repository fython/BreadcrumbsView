package moe.feng.common.view.breadcrumbs;

import android.content.Context;
import android.util.AttributeSet;

import androidx.recyclerview.widget.LinearLayoutManager;

class BreadcrumbsLayoutManager extends LinearLayoutManager {
    public BreadcrumbsLayoutManager(Context context) {
        super(context);
    }

    public BreadcrumbsLayoutManager(Context context, int orientation, boolean reverseLayout) {
        super(context, orientation, reverseLayout);
    }

    public BreadcrumbsLayoutManager(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    public boolean supportsPredictiveItemAnimations() {
        return false;
    }
}