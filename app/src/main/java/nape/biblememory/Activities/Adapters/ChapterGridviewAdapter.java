package nape.biblememory.Activities.Adapters;

import android.content.Context;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
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
    private BaseCallback chapterSelectedCallback;
    private TextView previousView;

    // Constructor
    public ChapterGridviewAdapter(Context c, List<String> chapterNums, int selectedPostion, BaseCallback chapterSelectedCB) {
        if(c != null) {
            mContext = c;
        }
        mChapterNums = chapterNums;
        this.selectedPosition = selectedPostion;
        this.chapterSelectedCallback = chapterSelectedCB;
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
        if(previousView == null && tView.getText().equals("1")){
            previousView = tView;
        }
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

        tView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tView.setBackgroundResource(R.drawable.chapter_circle);
                tView.setTextColor(mContext.getResources().getColor(R.color.colorWhite));
                previousView.setBackgroundResource(R.color.colorWhite);
                previousView.setTextColor(mContext.getResources().getColor(R.color.view_by_text_color));
                selectedPosition = Integer.valueOf(tView.getText().toString());
                chapterSelectedCallback.OnResponse(tView.getText());
                previousView = tView;
            }
        });
        return tView;
    }
}
