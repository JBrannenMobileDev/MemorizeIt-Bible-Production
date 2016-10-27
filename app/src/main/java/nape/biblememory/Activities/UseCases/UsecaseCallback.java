package nape.biblememory.Activities.UseCases;

import nape.biblememory.Activities.Models.ScriptureData;

/**
 * Created by Jonathan on 5/1/2016.
 */
public interface UsecaseCallback {
    void onSuccess(ScriptureData scripture);
    void onFailure(Exception error);
}
