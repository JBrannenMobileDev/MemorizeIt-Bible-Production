package nape.biblememory.Activities.Adapters;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;

import java.util.List;

import nape.biblememory.Activities.Interfaces.BaseCallback;
import nape.biblememory.R;


/**
 * Created by Jonathan on 10/17/2016.
 */

public class ChapterGridviewAdapter extends BaseAdapter {
    private Context mContext;
    private List<String> mChapterNums;
    private int selectedPosition;

    // Constructor
    public ChapterGridviewAdapter(Context c, List<String> chapterNums, int selectedPostion) {
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
        TextView tView;

        final float scale = mContext.getResources().getDisplayMetrics().density;
        int WH = (int) (45 * scale + 0.5f);
        int padding = (int) (2 * scale + 0.5f);

        if (convertView == null) {
            tView = new TextView(mContext);
        } else {
            tView = (TextView) convertView;
        }
        ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(WH,WH);
        tView.setText(mChapterNums.get(position));
        tView.setTextSize(18);
        tView.setPadding(padding, padding, padding, padding);
        tView.setGravity(Gravity.CENTER);
        tView.setLayoutParams(lp);
        if(position == selectedPosition) {
            tView.setBackgroundResource(R.drawable.chapter_circle);
            tView.setTextColor(mContext.getResources().getColor(R.color.colorWhite));
        }else{
            tView.setBackgroundResource(R.color.colorWhite);
            tView.setTextColor(mContext.getResources().getColor(R.color.view_by_text_color));
        }
        return tView;
    }
}
