package nape.biblememory.use_cases;

import nape.biblememory.models.MyVerse;

/**
 * Created by Jonathan on 5/1/2016.
 */
public interface UsecaseCallback {
    void onSuccess(MyVerse scripture);
    void onFailure(Exception error);
}
