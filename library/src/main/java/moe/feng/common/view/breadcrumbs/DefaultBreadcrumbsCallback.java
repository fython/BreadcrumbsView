package moe.feng.common.view.breadcrumbs;

import moe.feng.common.view.breadcrumbs.model.BreadcrumbItem;

public abstract class DefaultBreadcrumbsCallback implements BreadcrumbsCallback {

	@Override
	public void onItemClick(BreadcrumbsView view, int position) {
		view.removeItemBefore(position + 1);
		this.onNavigateBack(view.getItems().get(position), position);
	}

	public abstract void onNavigateBack(BreadcrumbItem item, int position);

}
