package nape.biblememory.Views;

import nape.biblememory.Activities.BaseCallback;

/**
 * Created by jbrannen on 8/4/17.
 */

public interface VerseFragmentCallback extends BaseCallback {
    void onResponse(Object verseNum, int selectedPosition);
}
