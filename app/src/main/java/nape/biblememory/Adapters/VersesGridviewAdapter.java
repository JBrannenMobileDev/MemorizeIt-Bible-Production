package nape.biblememory.Adapters;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import nape.biblememory.Activities.BaseCallback;
import nape.biblememory.R;
import nape.biblememory.Views.VerseFragmentCallback;


/**
 * Created by Jonathan on 10/17/2016.
 */

public class VersesGridviewAdapter extends BaseAdapter {
    private Context mContext;
    private List<String> mChapterNums;
    private int selectedPosition;
    private VerseFragmentCallback vSelectedCallback;

    public VersesGridviewAdapter(Context c, List<String> chapterNums, int selectedPostion, VerseFragmentCallback verseSelectedCallback) {
        vSelectedCallback = verseSelectedCallback;
        if(c != null) {
            mContext = c;
        }
        mChapterNums = chapterNums;
        this.selectedPosition = selectedPostion;
    }

    public int getCount() {
        return mChapterNums.size();
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 0;
    }

    public View getView(final int position, View convertView, ViewGroup parent) {
        final TextView tView;

        final float scale = mContext.getResources().getDisplayMetrics().density;
        int WH = (int) (45 * scale + 0.5f);
        int padding = (int) (2 * scale + 0.5f);

        tView = new TextView(mContext);

        ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(WH,WH);
        tView.setText(mChapterNums.get(position));
        tView.setTextSize(18);
        tView.setPadding(padding, padding, padding, padding);
        tView.setGravity(Gravity.CENTER);
        tView.setLayoutParams(lp);


        tView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (position == selectedPosition) {
                    tView.setBackgroundResource(R.drawable.chapter_circle);
                    tView.setTextColor(mContext.getResources().getColor(R.color.colorWhite));
                } else {
                    tView.setBackgroundResource(R.color.colorWhite);
                    tView.setTextColor(mContext.getResources().getColor(R.color.view_by_text_color));
                }
                tView.setEnabled(false);
                tView.setBackgroundResource(R.drawable.chapter_circle);
                tView.setTextColor(mContext.getResources().getColor(R.color.colorWhite));
                if(vSelectedCallback != null){
                    vSelectedCallback.onResponse(String.valueOf(mChapterNums.get(position)), position);
                }
            }
        });

        return tView;
    }
}
