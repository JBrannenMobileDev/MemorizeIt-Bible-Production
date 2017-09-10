package nape.biblememory.view_layer.fragments.dialogs;


import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TimePicker;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import nape.biblememory.R;

/**
 * Created by OWNER-PC on 12/19/2016.
 */

public class TimeSelectionDialogFragment extends DialogFragment {

    private TimePicker clock;
    private Button ok;
    private Button cancel;
    private TimeSelectedListener timeSelectedListener;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.time_selector, container);
        clock = (TimePicker) view.findViewById(R.id.timePicker);
        clock.setIs24HourView(false);
        ok = (Button) view.findViewById(R.id.ok_button);
        cancel = (Button) view.findViewById(R.id.cancel_button);
        timeSelectedListener = (TimeSelectedListener) getActivity();

        setOnclickListeners();
        return view;
    }

    private void setOnclickListeners() {
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String result;
                SimpleDateFormat formatter = new SimpleDateFormat("h:mm a");
                Calendar calendar = Calendar.getInstance();
                int hour;
                int minute;

                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                    hour = clock.getHour();
                }else{
                    hour = clock.getCurrentHour();
                }

                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                    minute = clock.getMinute();
                }else{
                    minute = clock.getCurrentMinute();
                }
                calendar.set(Calendar.HOUR_OF_DAY, hour);
                calendar.set(Calendar.MINUTE, minute);
                result = formatter.format(calendar.getTime());
                timeSelectedListener.onTimeSelected(result, calendar.getTime());
                TimeSelectionDialogFragment.this.getDialog().cancel();
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TimeSelectionDialogFragment.this.getDialog().cancel();
            }
        });
    }

    public interface TimeSelectedListener {
        void onTimeSelected(String time, Date date);
    }
}
