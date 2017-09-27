package nape.biblememory.view_layer.expandable_recyclerview;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.thoughtbot.expandablerecyclerview.ExpandableRecyclerViewAdapter;
import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup;

import java.util.List;

import nape.biblememory.R;
import nape.biblememory.models.MyVerse;

/**
 * Created by jbrannen on 9/27/17.
 */

public class CategoryAdapter extends ExpandableRecyclerViewAdapter<CategoryViewHolder, VerseViewHolder> {

    public CategoryAdapter(List<? extends ExpandableGroup> groups){
        super(groups);
    }

    @Override
    public CategoryViewHolder onCreateGroupViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.category_recycler_item, parent, false);
        return new CategoryViewHolder(view);
    }

    @Override
    public VerseViewHolder onCreateChildViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.verse_recycler_item, parent, false);
        return new VerseViewHolder(view);
    }

    @Override
    public void onBindChildViewHolder(VerseViewHolder holder, int flatPosition, ExpandableGroup group, int childIndex) {
        final MyVerse verse = (MyVerse)(group.getItems().get(childIndex));
        holder.onBind(verse);
    }

    @Override
    public void onBindGroupViewHolder(CategoryViewHolder holder, int flatPosition, ExpandableGroup group) {
        holder.setCategoryTitle(group);
    }
}
