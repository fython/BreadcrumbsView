package moe.feng.common.view.breadcrumbs;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import moe.feng.common.view.breadcrumbs.model.BreadcrumbItem;

import java.util.ArrayList;
import java.util.List;

public class BreadcrumbsView extends FrameLayout {

	private RecyclerView mRecyclerView;
	private BreadcrumbsAdapter mAdapter;

	private int mPopupThemeId = -1;

	public BreadcrumbsView(Context context) {
		this(context, null);
	}

	public BreadcrumbsView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public BreadcrumbsView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);

		if (attrs != null) {
			TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.BreadcrumbsView, defStyleAttr, 0);
			mPopupThemeId = a.getResourceId(R.styleable.BreadcrumbsView_popupTheme, -1);
			a.recycle();
		}

		init();
	}

	private void init() {
		if (mRecyclerView == null) {
			ViewGroup.LayoutParams rvLayoutParams = new ViewGroup.LayoutParams(-1, -1);
			rvLayoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
			mRecyclerView = new RecyclerView(getContext());

			LinearLayoutManager layoutManager = new LinearLayoutManager(
					getContext(), LinearLayoutManager.HORIZONTAL, ViewUtils.isRtlLayout(getContext()));
			mRecyclerView.setLayoutManager(layoutManager);
			mRecyclerView.setOverScrollMode(OVER_SCROLL_NEVER);

			addView(mRecyclerView, rvLayoutParams);
		}
		if (mAdapter == null) {
			mAdapter = new BreadcrumbsAdapter(this);
			if (mPopupThemeId != -1) {
				mAdapter.setPopupThemeId(mPopupThemeId);
			}
		}
		mRecyclerView.setAdapter(mAdapter);
	}

	public @Nullable List<BreadcrumbItem> getItems() {
		return mAdapter.getItems();
	}

	public void setItems(@Nullable ArrayList<BreadcrumbItem> items) {
		mAdapter.setItems(items);
		mAdapter.notifyDataSetChanged();
		postDelayed(new Runnable() {
			@Override
			public void run() {
				mRecyclerView.smoothScrollToPosition(mAdapter.getItemCount() - 1);
			}
		}, 500);
	}

	public void notifyItemChanged(int index) {
		mAdapter.notifyItemChanged(index * 2);
	}

	public void addItem(@NonNull BreadcrumbItem item) {
		int oldSize = mAdapter.getItemCount();
		mAdapter.getItems().add(item);
		mAdapter.notifyItemRangeInserted(oldSize, 2);
		mAdapter.notifyItemChanged(oldSize - 1);
		postDelayed(new Runnable() {
			@Override
			public void run() {
				mRecyclerView.smoothScrollToPosition(mAdapter.getItemCount() - 1);
			}
		}, 500);
	}

	public void removeItemBefore(final int beforePos) {
		if (beforePos <= mAdapter.getItems().size() - 1) {
			int oldSize = mAdapter.getItemCount();
			while (mAdapter.getItems().size() > beforePos) {
				mAdapter.getItems().remove(mAdapter.getItems().size() - 1);
			}
			mAdapter.notifyItemRangeRemoved(beforePos * 2 - 1, oldSize - beforePos);
			/* Add delay time to fix animation */
			postDelayed(new Runnable() {
				@Override
				public void run() {
					int currentPos = beforePos * 2 - 1 - 1;
					mAdapter.notifyItemChanged(currentPos);
					mRecyclerView.smoothScrollToPosition(currentPos);
				}
			}, 100);
		}
	}

	public void removeLastItem() {
		removeItemBefore(mAdapter.getItems().size() - 1);
	}

	public void setCallback(BreadcrumbsCallback callback) {
		mAdapter.setCallback(callback);
	}

	public BreadcrumbsCallback getCallback() {
		return mAdapter.getCallback();
	}

}
