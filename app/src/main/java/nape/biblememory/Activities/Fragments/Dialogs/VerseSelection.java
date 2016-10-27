package nape.biblememory.Activities.Fragments.Dialogs;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import nape.biblememory.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class VerseSelection extends Fragment {

    private Button newVerse;

    public VerseSelection() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_verse_selection, container, false);
        // Inflate the layout for this fragment
        newVerse = (Button) v.findViewById(R.id.new_verse_button);

        newVerse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((FragmentToActivity) getActivity()).onNewVerseClicked();
            }
        });
        return v;
    }

    public interface FragmentToActivity{
        void onBackPressed();
        void onNewVerseClicked();
    }
}
