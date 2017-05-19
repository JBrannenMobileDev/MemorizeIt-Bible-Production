package nape.biblememory.UseCases;

import nape.biblememory.Models.ScriptureData;

/**
 * Created by Jonathan on 5/1/2016.
 */
public interface UsecaseCallback {
    void onSuccess(ScriptureData scripture);
    void onFailure(Exception error);
}
