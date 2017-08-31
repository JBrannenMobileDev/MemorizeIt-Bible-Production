package nape.biblememory.Models;

import android.support.annotation.NonNull;

/**
 * Created by jbrannen on 8/21/17.
 */

public class User implements Comparable<User>{
    private String name;
    private String email;
    private String UID;
    private int totalVerses;

    public User(String displayName, String email, String uid, int i) {
        this.name = displayName;
        this.email = email;
        this.UID = uid;
        this.totalVerses = i;
    }

    public User() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUID() {
        return UID;
    }

    public void setUID(String UID) {
        this.UID = UID;
    }

    public int getTotalVerses() {
        return totalVerses;
    }

    public void setTotalVerses(int totalVerses) {
        this.totalVerses = totalVerses;
    }

    @Override
    public int compareTo(@NonNull User user) {
        return this.name.compareToIgnoreCase(user.getName());
    }
}
