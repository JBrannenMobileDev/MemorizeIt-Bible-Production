package nape.biblememory.view_layer.expandable_recyclerview;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup;
import com.thoughtbot.expandablerecyclerview.viewholders.GroupViewHolder;

import butterknife.BindView;
import butterknife.ButterKnife;
import nape.biblememory.R;

/**
 * Created by jbrannen on 9/27/17.
 */

public class CategoryViewHolder extends GroupViewHolder {
    @BindView(R.id.category_title) TextView categoryTitle;
    @BindView(R.id.category_expand_arrow) ImageView expandArrow;

    public CategoryViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
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
