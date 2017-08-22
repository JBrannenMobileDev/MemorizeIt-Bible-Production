package nape.biblememory.Models;

import java.util.List;

/**
 * Created by jbrannen on 8/18/17.
 */

public class Friend {
    private String name;
    private String email;
    private String uID;
    private List<ScriptureData> upcomingVerses;
    private List<ScriptureData> quizVerses;
    private List<ScriptureData> memorizedVerses;

    public String getuID() {
        return uID;
    }

    public void setuID(String uID) {
        this.uID = uID;
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

    public List<ScriptureData> getUpcomingVerses() {
        return upcomingVerses;
    }

    public void setUpcomingVerses(List<ScriptureData> upcomingVerses) {
        this.upcomingVerses = upcomingVerses;
    }

    public List<ScriptureData> getQuizVerses() {
        return quizVerses;
    }

    public void setQuizVerses(List<ScriptureData> quizVerses) {
        this.quizVerses = quizVerses;
    }

    public List<ScriptureData> getMemorizedVerses() {
        return memorizedVerses;
    }

    public void setMemorizedVerses(List<ScriptureData> memorizedVerses) {
        this.memorizedVerses = memorizedVerses;
    }
}
