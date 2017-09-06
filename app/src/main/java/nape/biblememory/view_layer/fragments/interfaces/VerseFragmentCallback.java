package nape.biblememory.view_layer.fragments.interfaces;

import nape.biblememory.view_layer.Activities.BaseCallback;

/**
 * Created by jbrannen on 8/4/17.
 */

public interface VerseFragmentCallback extends BaseCallback {
    void onResponse(Object verseNum, int selectedPosition);
}
