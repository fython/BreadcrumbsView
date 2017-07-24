package moe.feng.common.view.breadcrumbs.model;

import android.support.annotation.NonNull;

import java.util.Collections;
import java.util.List;

public class BreadcrumbItem {

	private int mSelectedIndex = -1;
	private List<String> mItems;

	public BreadcrumbItem(@NonNull List<String> items) {
		this(items, 0);
	}

	public BreadcrumbItem(@NonNull List<String> items, int selectedIndex) {
		if (items != null && !items.isEmpty()) {
			this.mItems = items;
			this.mSelectedIndex = selectedIndex;
		} else {
			throw new IllegalArgumentException("Items shouldn\'t be null empty.");
		}
	}

	public void setSelectedItem(String selectedItem) {
		this.mSelectedIndex = mItems.indexOf(selectedItem);
		if (mSelectedIndex == -1) {
			throw new IllegalArgumentException("This item does not exist in items.");
		}
	}

	public void setSelectedIndex(int selectedIndex) {
		this.mSelectedIndex = selectedIndex;
	}

	public int getSelectedIndex() {
		return this.mSelectedIndex;
	}

	public @NonNull String getSelectedItem() {
		return this.mItems.get(getSelectedIndex());
	}

	public boolean hasMoreSelect() {
		return this.mItems.size() > 1;
	}

	public void setItems(@NonNull List<String> items) {
		this.setItems(items, 0);
	}

	public void setItems(@NonNull List<String> items, int selectedIndex) {
		if (items != null && !items.isEmpty()) {
			this.mItems = items;
			this.mSelectedIndex = selectedIndex;
		} else {
			throw new IllegalArgumentException("Items shouldn\'t be null empty.");
		}
	}

	public List<String> getItems() {
		return mItems;
	}

	public static BreadcrumbItem createSimpleItem(String title) {
		return new BreadcrumbItem(Collections.singletonList(title));
	}

}
