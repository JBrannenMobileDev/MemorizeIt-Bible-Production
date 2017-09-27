package nape.biblememory.view_layer.expandable_recyclerview;

import android.view.View;
import android.widget.TextView;

import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup;
import com.thoughtbot.expandablerecyclerview.viewholders.GroupViewHolder;

import nape.biblememory.R;

/**
 * Created by jbrannen on 9/27/17.
 */

public class CategoryViewHolder extends GroupViewHolder {
    private TextView categoryTitle;

    public CategoryViewHolder(View itemView) {
        super(itemView);
        categoryTitle = (TextView)itemView.findViewById(R.id.category_title);
    }

    public void setCategoryTitle(ExpandableGroup group) {
        categoryTitle.setText(group.getTitle());
    }
}
