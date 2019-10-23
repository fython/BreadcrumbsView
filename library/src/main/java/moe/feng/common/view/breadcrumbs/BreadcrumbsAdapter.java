package moe.feng.common.view.breadcrumbs;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatDrawableManager;
import androidx.appcompat.widget.ListPopupWindow;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.recyclerview.widget.RecyclerView;
import moe.feng.common.view.breadcrumbs.model.IBreadcrumbItem;

class BreadcrumbsAdapter extends RecyclerView.Adapter<BreadcrumbsAdapter.ItemHolder> {

	private final int DROPDOWN_OFFSET_Y_FIX;

	private List<IBreadcrumbItem> items = new ArrayList<>();
	private BreadcrumbsCallback callback;

	private BreadcrumbsView parent;

	private int mPopupThemeId = -1;

	public BreadcrumbsAdapter(BreadcrumbsView parent) {
		this(parent, new ArrayList<IBreadcrumbItem>());
	}

	public BreadcrumbsAdapter(BreadcrumbsView parent, ArrayList<IBreadcrumbItem> items) {
		this.parent = parent;
		this.items = items;
		DROPDOWN_OFFSET_Y_FIX = parent.getResources().getDimensionPixelOffset(R.dimen.dropdown_offset_y_fix_value);
	}

	public @NonNull <E extends IBreadcrumbItem> List<E> getItems() {
		return (List<E>) this.items;
	}

	public <E extends IBreadcrumbItem> void setItems(@NonNull List<E> items) {
		this.items = (List<IBreadcrumbItem>) items;
	}

	public void setCallback(@Nullable BreadcrumbsCallback callback) {
		this.callback = callback;
	}

	public @Nullable BreadcrumbsCallback getCallback() {
		return this.callback;
	}

	public void setPopupThemeId(@IdRes int popupThemeId) {
		this.mPopupThemeId = popupThemeId;
	}

	@NonNull
    @Override
	public ItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
		LayoutInflater inflater = LayoutInflater.from(parent.getContext());
		if (viewType == R.layout.breadcrumbs_view_item_arrow) {
			return new ArrowIconHolder(inflater.inflate(viewType, parent, false));
		} else if (viewType == R.layout.breadcrumbs_view_item_text) {
			return new BreadcrumbItemHolder(inflater.inflate(viewType, parent, false));
		} else {
			throw new IllegalArgumentException("Unknown view type:" + viewType);
		}
	}

	@Override
	public void onBindViewHolder(@NonNull ItemHolder holder, int position) {
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

	class BreadcrumbItemHolder extends ItemHolder<IBreadcrumbItem> {

		TextView button;

		BreadcrumbItemHolder(View itemView) {
			super(itemView);
			button = (TextView) itemView;
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
		public void setItem(@NonNull IBreadcrumbItem item) {
			super.setItem(item);
			button.setText(item.getSelectedItem().toString());
			/*button.setTextColor(
					ViewUtils.getColorFromAttr(getContext(),
							getAdapterPosition() == getItemCount() - 1
									? android.R.attr.textColorPrimary : android.R.attr.textColorSecondary)
			);*/
			button.setTextColor(getAdapterPosition() == getItemCount() - 1 ? parent.getSelectedTextColor()
					: parent.getTextColor());
		}

	}

	class ArrowIconHolder extends ItemHolder<IBreadcrumbItem> {

		ImageButton imageButton;
		ListPopupWindow popupWindow;

		ArrowIconHolder(View itemView) {
			super(itemView);
			Drawable normalDrawable = AppCompatDrawableManager.get().getDrawable(getContext(), R.drawable.ic_chevron_right_black_24dp);
			// Drawable normalDrawable = getContext().getResources().getDrawable(R.drawable.ic_chevron_right_black_24dp);
			Drawable wrapDrawable = DrawableCompat.wrap(normalDrawable);
			// DrawableCompat.setTint(wrapDrawable, ViewUtils.getColorFromAttr(getContext(), android.R.attr.textColorSecondary));
			DrawableCompat.setTintList(wrapDrawable, parent.getTextColor());
			imageButton = (ImageButton) itemView;
			imageButton.setImageDrawable(wrapDrawable);
			imageButton.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View view) {
					if (item.hasMoreSelect()) {
						try {
							popupWindow.show();
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}
			});
			createPopupWindow();
		}

		@Override
		public void setItem(@NonNull IBreadcrumbItem item) {
			super.setItem(item);
			imageButton.setClickable(item.hasMoreSelect());
			if (item.hasMoreSelect()) {
				List<Map<String, String>> list = new ArrayList<>();
				for (Object obj : item.getItems()) {
					Map<String, String> map = new HashMap<>();
					map.put("text", obj.toString());
					list.add(map);
				}
				ListAdapter adapter = new SimpleAdapter(getPopupThemedContext(), list, R.layout.breadcrumbs_view_dropdown_item, new String[] {"text"}, new int[] {android.R.id.text1});
				popupWindow.setAdapter(adapter);
				popupWindow.setWidth(ViewUtils.measureContentWidth(getPopupThemedContext(), adapter));
				imageButton.setOnTouchListener(popupWindow.createDragToOpenListener(imageButton));
			} else {
				imageButton.setOnTouchListener(null);
			}
		}

		private void createPopupWindow() {
			popupWindow = new ListPopupWindow(getPopupThemedContext());
			popupWindow.setAnchorView(imageButton);
			popupWindow.setModal(true);
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
					popupWindow.setVerticalOffset(-imageButton.getMeasuredHeight() + DROPDOWN_OFFSET_Y_FIX);
					imageButton.getViewTreeObserver().removeOnGlobalLayoutListener(this);
				}
			});
		}

	}

	class ItemHolder<T> extends RecyclerView.ViewHolder {

		T item;

		ItemHolder(View itemView) {
			super(itemView);
		}

		public void setItem(@NonNull T item) {
			this.item = item;
		}

		Context getContext() {
			return itemView.getContext();
		}

		Context getPopupThemedContext() {
			return mPopupThemeId != -1 ? new ContextThemeWrapper(getContext(), mPopupThemeId) : getContext();
		}

	}

}
