package moe.feng.common.view.breadcrumbs;

import java.util.List;

import moe.feng.common.view.breadcrumbs.model.IBreadcrumbItem;

class BreadcrumbsUtil {
    static int getAdapterCount(List<IBreadcrumbItem> item) {
        return (item != null && !item.isEmpty()) ? (item.size() * 2 - 1) : 0;
    }

    static int getItemViewType(int position) {
        return position % 2 == 1 ? R.layout.breadcrumbs_view_item_arrow : R.layout.breadcrumbs_view_item_text;
    }

    static int getTruePosition(int viewType, int position) {
       return viewType == R.layout.breadcrumbs_view_item_arrow ? ((position - 1) / 2) + 1 : position / 2;
    }
}
