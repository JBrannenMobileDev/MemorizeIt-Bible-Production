package nape.biblememory.Activities.Adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import nape.biblememory.Activities.BaseCallback;
import nape.biblememory.Activities.Models.ScriptureData;
import nape.biblememory.R;

public class RecyclerViewAdapterBookList extends RecyclerView.Adapter<RecyclerViewAdapterBookList.ViewHolder> {
    private static final String PLURAL_FORM = "verses memorized";
    private static final String SINGULAR_FORM = "verse memorized";
    private List<String> bookList;
    private List<ScriptureData> verseList;
    private BaseCallback bookSelectedCallback;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView bookName;
        public TextView numberMemorized;
        public TextView versesMemorized;
        public ViewHolder(View v, final BaseCallback bookSelectedCallback) {
            super(v);
            bookName = (TextView) v.findViewById(R.id.book_name);
            numberMemorized = (TextView) v.findViewById(R.id.number_memorized_textview);
            versesMemorized = (TextView) v.findViewById(R.id.verses_memorized_textview);

            v.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    bookSelectedCallback.onResponse(bookName.getText());
                }
            });
        }
    }

    public RecyclerViewAdapterBookList(List<String> bookList, List<ScriptureData> dataset, BaseCallback bookSelectedCallback){
        this.bookList = bookList;
        this.verseList = dataset;
        this.bookSelectedCallback = bookSelectedCallback;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public RecyclerViewAdapterBookList.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view_book_name, parent, false);

        // set the view's size, margins, paddings and layout parameters
        ViewHolder vh = new ViewHolder(v, bookSelectedCallback);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        String bookName = bookList.get(position);
        int occurrences = Collections.frequency(getAllVerseBooks(verseList), bookName);
        holder.bookName.setText(bookName);
        holder.numberMemorized.setText(Integer.toString(occurrences));
        if(occurrences == 1){
            holder.versesMemorized.setText(SINGULAR_FORM);
        }else{
            holder.versesMemorized.setText(PLURAL_FORM);
        }
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return this.bookList.size();
    }

    public List<String> getAllVerseBooks(List<ScriptureData> dataSet) {
        List<String> bookList = new ArrayList<>();
        for(ScriptureData verse : dataSet){
            if(verse.getVerseLocation() != null){
                String verseLocation = verse.getVerseLocation();
                boolean parsedBookName = false;

                int index = 0;
                while(!parsedBookName){
                    if((verseLocation.charAt(index) == 1) || (verseLocation.charAt(index) == 2) || (verseLocation.charAt(index) == 3)){
                        parsedBookName = false;
                    }else if(verseLocation.charAt(index) == ' '){
                        String parsedBookNameString = verseLocation.substring(0,index);
                        bookList.add(parsedBookNameString);
                        parsedBookName = true;
                    }
                    index++;
                }
            }
        }
        return bookList;
    }

}