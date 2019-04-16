package moe.feng.common.view.breadcrumbs;

import java.util.List;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DiffUtil;
import moe.feng.common.view.breadcrumbs.model.IBreadcrumbItem;

public class BreadcrumbsDiffCallback extends DiffUtil.Callback {

    public static String PAYLOAD = "BreadcrumbsDiffCallback_Payload";

    private List<IBreadcrumbItem> oldItems;
    private List<IBreadcrumbItem> newItems;

    public BreadcrumbsDiffCallback(List<IBreadcrumbItem> newItems, List<IBreadcrumbItem> oldItems) {
        this.newItems = newItems;
        this.oldItems = oldItems;
    }

    @Override
    public int getOldListSize() {
        return BreadcrumbsUtil.getAdapterCount(oldItems);
    }

    @Override
    public int getNewListSize() {
        return BreadcrumbsUtil.getAdapterCount(newItems);
    }

    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        // we consider same positions as same items
        return oldItemPosition == newItemPosition;
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        int type1 = BreadcrumbsUtil.getItemViewType(oldItemPosition);
        int type2 = BreadcrumbsUtil.getItemViewType(newItemPosition);
        if (type1 != type2) {
            return false;
        }
        int pos1 = BreadcrumbsUtil.getTruePosition(type1, oldItemPosition);
        int pos2 = BreadcrumbsUtil.getTruePosition(type2, newItemPosition);
        return oldItems.get(pos1).equals(newItems.get(pos2));
    }

    @Nullable
    @Override
    public Object getChangePayload(int oldItemPosition, int newItemPosition) {
        return PAYLOAD;
    }
}