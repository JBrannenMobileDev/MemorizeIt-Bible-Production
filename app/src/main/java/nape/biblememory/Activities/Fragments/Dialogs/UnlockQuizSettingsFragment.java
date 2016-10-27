package nape.biblememory.Activities.Fragments.Dialogs;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import nape.biblememory.R;

/**
 * Created by hp1 on 21-01-2015.
 */
public class UnlockQuizSettingsFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v =inflater.inflate(R.layout.fragment_unlock_quiz_settings,container,false);
        return v;
    }
}