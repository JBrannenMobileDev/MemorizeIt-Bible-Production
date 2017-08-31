package nape.biblememory.utils;

import android.content.res.Resources;

/**
 * Created by jbrannen on 8/31/17.
 */

public class DpConverterUtil {
    public static int dpToPx(int dp)
    {
        return (int) (dp * Resources.getSystem().getDisplayMetrics().density);
    }

    public static int pxToDp(int px)
    {
        return (int) (px / Resources.getSystem().getDisplayMetrics().density);
    }
}
