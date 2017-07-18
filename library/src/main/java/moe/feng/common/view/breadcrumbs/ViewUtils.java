package moe.feng.common.view.breadcrumbs;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.support.annotation.AttrRes;
import android.view.View;

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

}
