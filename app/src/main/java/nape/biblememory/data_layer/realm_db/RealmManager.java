package nape.biblememory.data_layer.realm_db;


import java.util.List;

import io.realm.Realm;
import io.realm.RealmQuery;
import io.realm.RealmResults;
import nape.biblememory.models.MemorizedVerse;
import nape.biblememory.models.MyVerse;
import nape.biblememory.models.Notification;
import nape.biblememory.models.ScriptureData;
import nape.biblememory.models.User;

/**
 * Created by jbrannen on 8/29/17.
 */

public class RealmManager {

    private Realm realm;

    public RealmManager() {
        realm = Realm.getDefaultInstance();
    }


    public MyVerse getVerse(String verseLocation){
        return realm.where(MyVerse.class).equalTo("verseLocation", verseLocation).findFirst();
    }

    public User getUser(String uid){
        return realm.where(User.class).equalTo("uid", uid).findFirst();
    }

    public RealmResults<MyVerse> getVerses(){
        if(realm.isClosed()){
            realm = Realm.getDefaultInstance();
        }
        return realm.where(MyVerse.class).findAll();
    }

    public RealmResults<User> getUsers(){
        return realm.where(User.class).findAll();
    }

    public RealmResults<Notification> getNotifications(){
        return realm.where(Notification.class).findAll();
    }

    public void insertOrUpdateVerse(final MyVerse verse){
        if(realm.isClosed()){
            realm = Realm.getDefaultInstance();
        }
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm bgRealm) {
                bgRealm.copyToRealmOrUpdate(verse);
            }
        });
    }

    public void insertOrUpdateUser(final User user){
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm bgRealm) {
                bgRealm.copyToRealmOrUpdate(user);
            }
        });
    }

    public void insertOrUpdateNotif(final Notification notification){
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm bgRealm) {
                bgRealm.copyToRealmOrUpdate(notification);
            }
        });
    }


    public void insertOrUpdateMyVerses(final List<MyVerse> verses){
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm bgRealm) {
                for(MyVerse verse : verses){
                    bgRealm.copyToRealmOrUpdate(verse);
                }

            }
        });
    }

    public void insertOrUpdateMyVersesWithScriptureData(final List<ScriptureData> verses){
        if(realm.isClosed()){
            realm = Realm.getDefaultInstance();
        }
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm bgRealm) {
                for(ScriptureData verse : verses){
                    bgRealm.copyToRealmOrUpdate(verse.toMyVerse());
                }

            }
        });
    }

    public void insertOrUpdateMemorizedVerses(final List<MemorizedVerse> verses){
        if(realm.isClosed()){
            realm = Realm.getDefaultInstance();
        }
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm bgRealm) {
                for(MemorizedVerse verse : verses){
                    bgRealm.copyToRealmOrUpdate(verse);
                }

            }
        });
    }

    public void insertOrUpdateMemorizedVerse(final MemorizedVerse verse){
        if(realm.isClosed()){
            realm = Realm.getDefaultInstance();
        }
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm bgRealm) {
                bgRealm.copyToRealmOrUpdate(verse);
            }
        });
    }


    public void insertOrUpdateUsers(final List<User> users){
        if(realm.isClosed()){
            realm = Realm.getDefaultInstance();
        }
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm bgRealm) {
                for(User user : users){
                    bgRealm.copyToRealmOrUpdate(user);
                }

            }
        });
    }

    public void insertOrUpdateNotifs(final List<Notification> notifications){
        if(realm.isClosed()){
            realm = Realm.getDefaultInstance();
        }
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm bgRealm) {
                for(Notification notification : notifications){
                    bgRealm.copyToRealmOrUpdate(notification);
                }

            }
        });
    }

    public void deleteQuizVerse(final MyVerse verse){
        Realm realm = Realm.getDefaultInstance();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                RealmQuery<MyVerse> versetoDelete = realm.where(MyVerse.class).contains("verseLocation", verse.getVerseLocation());
                if(versetoDelete.findFirst() != null) {
                    versetoDelete.findFirst().deleteFromRealm();
                }
            }
        });
    }

    public void deleteMemorizedVerse(final MemorizedVerse verse) {
        Realm realm = Realm.getDefaultInstance();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                RealmQuery<MemorizedVerse> versetoDelete = realm.where(MemorizedVerse.class).contains("verseLocation", verse.getVerseLocation());
                if(versetoDelete.findFirst() != null) {
                    versetoDelete.findFirst().deleteFromRealm();
                }
            }
        });
    }

    public void deleteUser(final User user){
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm bgRealm) {
                user.deleteFromRealm();
            }
        });
    }

    public void deleteNotification(final Notification notification){
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm bgRealm) {
                notification.deleteFromRealm();
            }
        });
    }


    public void nukeDb() {
        realm.close();
        while(!realm.isClosed()){
            realm.close();
        }
        Realm.deleteRealm(Realm.getDefaultConfiguration());
        openRealm();
    }

    private void openRealm(){
        realm = Realm.getDefaultInstance();
    }
}
