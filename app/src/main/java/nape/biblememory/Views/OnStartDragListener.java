package nape.biblememory.Views;

import android.support.v7.widget.RecyclerView;

/**
 * Created by jbrannen on 9/2/17.
 */

public interface OnStartDragListener {

    /**
     * Called when a view is requesting a start of a drag.
     *
     * @param viewHolder The holder of the view to drag.
     */
    void onStartDrag(RecyclerView.ViewHolder viewHolder);

}