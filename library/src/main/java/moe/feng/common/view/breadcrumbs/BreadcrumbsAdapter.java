package moe.feng.common.view.breadcrumbs;

import android.content.Context;
import android.support.v7.widget.ListPopupWindow;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.*;
import moe.feng.common.view.breadcrumbs.model.BreadcrumbItem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class BreadcrumbsAdapter extends RecyclerView.Adapter<BreadcrumbsAdapter.ItemHolder> {

	private List<BreadcrumbItem> items;
	private BreadcrumbsCallback callback;

	private BreadcrumbsView parent;

	public BreadcrumbsAdapter(BreadcrumbsView parent) {
		this.parent = parent;
		this.items = new ArrayList<>();
	}

	public BreadcrumbsAdapter(BreadcrumbsView parent, ArrayList<BreadcrumbItem> items) {
		this.parent = parent;
		this.items = items;
	}

	public List<BreadcrumbItem> getItems() {
		return this.items;
	}

	public void setItems(ArrayList<BreadcrumbItem> items) {
		this.items = items;
	}

	public void setCallback(BreadcrumbsCallback callback) {
		this.callback = callback;
	}

	public BreadcrumbsCallback getCallback() {
		return this.callback;
	}

	@Override
	public ItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		LayoutInflater inflater = LayoutInflater.from(parent.getContext());
		if (viewType == R.layout.breadcrumbs_view_item_arrow) {
			return new ArrowIconHolder(inflater.inflate(viewType, parent, false));
		} else if (viewType == R.layout.breadcrumbs_view_item_text) {
			return new BreadcrumbItemHolder(inflater.inflate(viewType, parent, false));
		} else {
			return null;
		}
	}

	@Override
	public void onBindViewHolder(ItemHolder holder, int position) {
		int viewType = getItemViewType(position);
		int truePos = viewType == R.layout.breadcrumbs_view_item_arrow ? ((position - 1) / 2) + 1 : position / 2;
		holder.setItem(items.get(truePos));
	}

	@Override
	public int getItemCount() {
		return (items != null && !items.isEmpty()) ? (items.size() * 2 - 1) : 0;
	}

	@Override
	public int getItemViewType(int position) {
		return position % 2 == 1 ? R.layout.breadcrumbs_view_item_arrow : R.layout.breadcrumbs_view_item_text;
	}

	class BreadcrumbItemHolder extends ItemHolder<BreadcrumbItem> {

		public Button button;

		public BreadcrumbItemHolder(View itemView) {
			super(itemView);
			button = (Button) itemView;
			button.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View view) {
					if (callback != null) {
						callback.onItemClick(parent, getAdapterPosition() / 2);
					}
				}
			});
		}

		@Override
		public void setItem(BreadcrumbItem item) {
			super.setItem(item);
			button.setText(item.getSelectedItemTitle());
			Log.i("TAG", "title:" + item.getSelectedItemTitle() + " alpha:" + (getAdapterPosition() == getItemCount() - 1 ? .87f : .54f));
			button.setTextColor(
					ViewUtils.getColorFromAttr(getContext(),
							getAdapterPosition() == getItemCount() - 1
									? android.R.attr.textColorPrimary : android.R.attr.textColorSecondary)
			);
		}

	}

	class ArrowIconHolder extends ItemHolder<BreadcrumbItem> {

		public ImageButton imageButton;
		ListPopupWindow popupWindow;

		public ArrowIconHolder(View itemView) {
			super(itemView);
			imageButton = (ImageButton) itemView;
			imageButton.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View view) {
					if (item.hasMoreSelect()) {
						popupWindow.show();
					}
				}
			});
			popupWindow = new ListPopupWindow(getContext());
			popupWindow.setAnchorView(imageButton);
			popupWindow.setOnItemClickListener(new AdapterView.OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
					if (callback != null) {
						callback.onItemChange(parent, getAdapterPosition() / 2, getItems().get(getAdapterPosition() / 2 + 1).getItems().get(i));
						popupWindow.dismiss();
					}
				}
			});
			imageButton.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
				@Override
				public void onGlobalLayout() {
					popupWindow.setVerticalOffset(-imageButton.getMeasuredHeight());
					imageButton.getViewTreeObserver().removeOnGlobalLayoutListener(this);
				}
			});
		}

		@Override
		public void setItem(BreadcrumbItem item) {
			super.setItem(item);
			imageButton.setClickable(item.hasMoreSelect());
			if (item.hasMoreSelect()) {
				List<Map<String, String>> list = new ArrayList<>();
				for (Object obj : item.getItems()) {
					Map<String, String> map = new HashMap<>();
					map.put("text", obj.toString());
					list.add(map);
				}
				// Kotlin: item.getItems().map { "text" to it.toString() }
				ListAdapter adapter = new SimpleAdapter(getContext(), list, android.R.layout.simple_list_item_1, new String[] {"text"}, new int[] {android.R.id.text1});
				popupWindow.setAdapter(adapter);
				popupWindow.setWidth(ViewUtils.measureContentWidth(getContext(), adapter));
				imageButton.setOnTouchListener(popupWindow.createDragToOpenListener(imageButton));
			} else {
				imageButton.setOnTouchListener(null);
			}
		}

	}

	class ItemHolder<T> extends RecyclerView.ViewHolder {

		protected T item;

		public ItemHolder(View itemView) {
			super(itemView);
		}

		public void setItem(T item) {
			this.item = item;
		}

		protected Context getContext() {
			return itemView.getContext();
		}

	}

}
