package moe.feng.common.view.breadcrumbs;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.support.annotation.AttrRes;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ListAdapter;

class ViewUtils {

	static boolean isRtlLayout(Context context) {
		return context.getResources().getConfiguration().getLayoutDirection() == View.LAYOUT_DIRECTION_RTL;
	}

	static int getColorFromAttr(Context context, @AttrRes int attr) {
		TypedArray array = context.getTheme().obtainStyledAttributes(new int[]{attr});
		int color = array.getColor(0, Color.TRANSPARENT);
		array.recycle();
		return color;
	}

	static int measureContentWidth(Context context, ListAdapter listAdapter) {
		ViewGroup mMeasureParent = null;
		int maxWidth = 0;
		View itemView = null;
		int itemType = 0;

		final ListAdapter adapter = listAdapter;
		final int widthMeasureSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
		final int heightMeasureSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
		final int count = adapter.getCount();
		for (int i = 0; i < count; i++) {
			final int positionType = adapter.getItemViewType(i);
			if (positionType != itemType) {
				itemType = positionType;
				itemView = null;
			}

			if (mMeasureParent == null) {
				mMeasureParent = new FrameLayout(context);
			}

			itemView = adapter.getView(i, itemView, mMeasureParent);
			itemView.measure(widthMeasureSpec, heightMeasureSpec);

			final int itemWidth = itemView.getMeasuredWidth();

			if (itemWidth > maxWidth) {
				maxWidth = itemWidth;
			}
		}

		return maxWidth;
	}

}
