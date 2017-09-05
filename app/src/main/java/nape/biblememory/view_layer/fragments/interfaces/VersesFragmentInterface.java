package nape.biblememory.view_layer.fragments.interfaces;

import java.util.List;

import nape.biblememory.Models.ScriptureData;

/**
 * Created by jbrannen on 9/3/17.
 */

public interface VersesFragmentInterface {
    void onReceivedRecyclerData(List<ScriptureData> myVerses);
}
