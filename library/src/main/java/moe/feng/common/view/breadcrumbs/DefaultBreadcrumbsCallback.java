package moe.feng.common.view.breadcrumbs;

import moe.feng.common.view.breadcrumbs.model.BreadcrumbItem;

public abstract class DefaultBreadcrumbsCallback implements BreadcrumbsCallback {

	@Override
	public void onItemClick(BreadcrumbsView view, int position) {
		if (position == view.getItems().size() - 1) return;
		view.removeItemAfter(position + 1);
		this.onNavigateBack(view.getItems().get(position), position);
	}

	@Override
	public void onItemChange(BreadcrumbsView view, int parentPosition, String nextItem) {
		BreadcrumbItem nextBreadcrumb = view.getItems().get(parentPosition + 1);
		nextBreadcrumb.setSelectedItem(nextItem);
		view.removeItemAfter(parentPosition + 2);
		if (parentPosition + 2 >= view.getItems().size()) {
			view.notifyItemChanged(parentPosition + 1);
		}
		this.onNavigateNewLocation(nextBreadcrumb, parentPosition + 1);
	}

	public abstract void onNavigateBack(BreadcrumbItem item, int position);
	public abstract void onNavigateNewLocation(BreadcrumbItem newItem, int changedPosition);

}
