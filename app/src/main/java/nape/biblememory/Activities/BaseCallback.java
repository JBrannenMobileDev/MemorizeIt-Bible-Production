package nape.biblememory.Activities;

/**
 * Created by Jonathan on 12/4/2016.
 */

public interface BaseCallback<T> {
    void onResponse(T response);
    void onFailure(Exception e);
}
