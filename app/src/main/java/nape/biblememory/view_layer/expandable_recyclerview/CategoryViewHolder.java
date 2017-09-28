package nape.biblememory.view_layer.expandable_recyclerview;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup;
import com.thoughtbot.expandablerecyclerview.viewholders.GroupViewHolder;

import nape.biblememory.R;

/**
 * Created by jbrannen on 9/27/17.
 */

public class CategoryViewHolder extends GroupViewHolder {
    private TextView categoryTitle;
    private ImageView expandArrow;

    public CategoryViewHolder(View itemView) {
        super(itemView);
        categoryTitle = (TextView)itemView.findViewById(R.id.category_title);
        expandArrow = (ImageView)itemView.findViewById(R.id.category_expand_arrow);
    }

    public void setCategoryTitle(ExpandableGroup group) {
        categoryTitle.setText(group.getTitle());
    }

    @Override
    public void expand() {
        expandArrow.animate().rotation(180f);
    }

    @Override
    public void collapse() {
        expandArrow.animate().rotation(0);
    }
}
