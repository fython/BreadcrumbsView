package moe.feng.common.view.breadcrumbs;

public interface BreadcrumbsCallback {

	void onItemClick(BreadcrumbsView view, int position);
	void onItemChange(BreadcrumbsView view, int parentPosition, String nextItem);

}
