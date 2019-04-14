package moe.feng.common.view.breadcrumbs.model;

import android.os.Parcelable;
import androidx.annotation.NonNull;

import java.util.List;

public interface IBreadcrumbItem<E> extends Parcelable, Iterable {

    /**
     * Select a item by index
     *
     * @param selectedIndex The index of the item should be selected
     */
    void setSelectedIndex(int selectedIndex);

    /**
     * Get selected item index
     *
     * @return The index of selected item
     */
    int getSelectedIndex();

    /**
     * Select a item
     *
     * @param selectedItem The item should be selected
     */
    void setSelectedItem(@NonNull E selectedItem);

    /**
     * Get selected item
     *
     * @return The selected item
     */
    @NonNull E getSelectedItem();

    /**
     * Check if there are other items
     *
     * @return Result
     */
    boolean hasMoreSelect();

    /**
     * Set a new items list
     *
     * @param items Items list
     */
    void setItems(@NonNull List<E> items);

    /**
     * Set a new items list with selecting a item
     *
     * @param items Items list
     * @param selectedIndex The selected item index
     */
    void setItems(@NonNull List<E> items, int selectedIndex);

    /**
     * Get items list
     *
     * @return Items List
     */
    List<E> getItems();

}
