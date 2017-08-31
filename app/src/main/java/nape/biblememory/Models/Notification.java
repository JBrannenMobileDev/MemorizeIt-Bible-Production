package nape.biblememory.Models;

/**
 * Created by jbrannen on 8/31/17.
 */

public class Notification {
    public static final String BLESSING = "blessing";
    public static final String COPIED_A_VERSE = "copied_a_verse";
    public static final String COPIED_ALL_VERSES = "copied_all_verses";
    private String notificationType;

    public String getNotificationType() {
        return notificationType;
    }

    public void setNotificationType(String notificationType) {
        this.notificationType = notificationType;
    }
}
