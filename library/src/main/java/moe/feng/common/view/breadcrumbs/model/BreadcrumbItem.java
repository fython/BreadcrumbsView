package moe.feng.common.view.breadcrumbs.model;

import android.os.Parcel;
import android.support.annotation.NonNull;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class BreadcrumbItem implements IBreadcrumbItem<String> {

	private int mSelectedIndex = -1;
	private List<String> mItems;

	public BreadcrumbItem(@NonNull List<String> items) {
		this(items, 0);
	}

	public BreadcrumbItem(@NonNull List<String> items, int selectedIndex) {
		if (!items.isEmpty()) {
			this.mItems = items;
			this.mSelectedIndex = selectedIndex;
		} else {
			throw new IllegalArgumentException("Items shouldn\'t be null empty.");
		}
	}

    private BreadcrumbItem(Parcel in) {
        mSelectedIndex = in.readInt();
        mItems = in.createStringArrayList();
    }

    public static final Creator<BreadcrumbItem> CREATOR = new Creator<BreadcrumbItem>() {
        @Override
        public BreadcrumbItem createFromParcel(Parcel in) {
            return new BreadcrumbItem(in);
        }

        @Override
        public BreadcrumbItem[] newArray(int size) {
            return new BreadcrumbItem[size];
        }
    };

	@Override
	public void setSelectedItem(@NonNull String selectedItem) {
		this.mSelectedIndex = mItems.indexOf(selectedItem);
		if (mSelectedIndex == -1) {
			throw new IllegalArgumentException("This item does not exist in items.");
		}
	}

    @Override
	public void setSelectedIndex(int selectedIndex) {
		this.mSelectedIndex = selectedIndex;
	}

    @Override
	public int getSelectedIndex() {
		return this.mSelectedIndex;
	}

    @Override
	public @NonNull String getSelectedItem() {
		return this.mItems.get(getSelectedIndex());
	}

    @Override
	public boolean hasMoreSelect() {
		return this.mItems.size() > 1;
	}

    @Override
	public void setItems(@NonNull List<String> items) {
		this.setItems(items, 0);
	}

    @Override
	public void setItems(@NonNull List<String> items, int selectedIndex) {
		if (!items.isEmpty()) {
			this.mItems = items;
			this.mSelectedIndex = selectedIndex;
		} else {
			throw new IllegalArgumentException("Items shouldn\'t be null empty.");
		}
	}

    @Override
	public @NonNull List<String> getItems() {
		return mItems;
	}

	/**
	 * Create a simple BreadcrumbItem with single item
	 *
	 * @param title Item title
	 * @return Simple BreadcrumbItem
	 * @see BreadcrumbItem
	 */
	public static BreadcrumbItem createSimpleItem(@NonNull String title) {
		return new BreadcrumbItem(Collections.singletonList(title));
	}

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(mSelectedIndex);
        dest.writeStringList(mItems);
    }

    @NonNull
    @Override
    public Iterator iterator() {
        return mItems.iterator();
    }
}
