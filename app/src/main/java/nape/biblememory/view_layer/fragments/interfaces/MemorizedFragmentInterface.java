package nape.biblememory.view_layer.fragments.interfaces;

import java.util.List;

import nape.biblememory.models.MemorizedVerse;
import nape.biblememory.models.MyVerse;

/**
 * Created by jbrannen on 9/3/17.
 */

public interface MemorizedFragmentInterface {
    void onReceivedRecyclerData(List<MemorizedVerse> myVerses);
}
