package nape.biblememory.data_store.FirebaseDb;

import android.content.Context;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import nape.biblememory.Activities.BaseCallback;
import nape.biblememory.Models.Friend;
import nape.biblememory.Models.ScriptureData;
import nape.biblememory.Models.User;
import nape.biblememory.Models.UserPreferencesModel;
import nape.biblememory.UserPreferences;

/**
 * Created by jbrannen on 8/7/17.
 */

public class FirebaseDb {
    private static final FirebaseDb ourInstance = new FirebaseDb();
    public static FirebaseDb getInstance() {
        return ourInstance;
    }

    private DatabaseReference friendsReference;
    private DatabaseReference usersReference;
    private DatabaseReference upcomingVersesReference;
    private DatabaseReference quizVersesReference;
    private DatabaseReference memorizedVersesReference;
    private DatabaseReference forgottenVersesReference;
    private DatabaseReference userPrefsReference;

    private UserPreferences mPrefs;

    private FirebaseDb() {
        mPrefs = new UserPreferences();
    }

    public void addNewUser(final User userData){
        BaseCallback<List<User>> usersCallback = new BaseCallback<List<User>>() {
            @Override
            public void onResponse(List<User> response) {
                boolean userExists = false;
                for(User user : response){
                    if(user.getUID().equals(userData.getUID())){
                        userExists = true;
                    }
                }
                if(!userExists){
                    usersReference = FirebaseDatabase.getInstance().getReference().child(Constants.FIREBASE_CHILD_USER_DATA);
                    usersReference.push().setValue(userData);
                }
            }

            @Override
            public void onFailure(Exception e) {

            }
        };
        getUsers(usersCallback);
    }

    public void getUsers(final BaseCallback<List<User>> userDataCallback) {
        final List<User> users = new ArrayList<>();
        usersReference = FirebaseDatabase.getInstance().getReference().child(Constants.FIREBASE_CHILD_USER_DATA);
        usersReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot data : dataSnapshot.getChildren()) {
                    User user = data.getValue(User.class);
                    users.add(user);
                }
                userDataCallback.onResponse(users);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void getUsers(final List<String> uidList, final BaseCallback<List<User>> userDataCallback) {
        final List<User> users = new ArrayList<>();
        usersReference = FirebaseDatabase.getInstance().getReference().child(Constants.FIREBASE_CHILD_USER_DATA);
        usersReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot data : dataSnapshot.getChildren()) {
                    User user;
                    user = data.getValue(User.class);
                    if(uidList.contains(user.getUID())) {
                        users.add(user);
                    }
                }
                userDataCallback.onResponse(users);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void updateUserdata(String UID, final int updateAmount){
        usersReference = FirebaseDatabase.getInstance().getReference().child(Constants.FIREBASE_CHILD_USER_DATA);
        usersReference.orderByChild("UID").equalTo(UID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot data : dataSnapshot.getChildren()) {
                    User user = data.getValue(User.class);
                    HashMap<String, Object> result = new HashMap<>();
                    result.put("totalVerses", user.getTotalVerses() + updateAmount);
                    FirebaseDatabase.getInstance().getReference().child(Constants.FIREBASE_CHILD_USER_DATA)
                            .child(data.getKey()).updateChildren(result);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void getFriends(final BaseCallback<List<String>> friendsCallback, Context context){
        friendsReference = FirebaseDatabase.getInstance().getReference().child(Constants.FIREBASE_CHILD_USERS).
                child(mPrefs.getUserId(context)).child(Constants.FRIENDS);
        friendsReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<String> uidList = new ArrayList<>();
                for(DataSnapshot data : dataSnapshot.getChildren()) {
                    String uid = data.getValue(String.class);
                    uidList.add(uid);
                }
                friendsCallback.onResponse(uidList);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void addFriend(String uid, Context applicationContext){
        friendsReference = FirebaseDatabase.getInstance().getReference().child(Constants.FIREBASE_CHILD_USERS).
                child(mPrefs.getUserId(applicationContext)).child(Constants.FRIENDS);
        friendsReference.push().setValue(uid);
    }

    public void deleteFriend(String uid, final Context applicationContext){
        friendsReference = FirebaseDatabase.getInstance().getReference().child(Constants.FIREBASE_CHILD_USERS).
                child(mPrefs.getUserId(applicationContext)).child(Constants.FRIENDS);
        friendsReference.orderByChild("uid").equalTo(uid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot data : dataSnapshot.getChildren()) {
                    FirebaseDatabase.getInstance().getReference().child(Constants.FIREBASE_CHILD_USERS).child(mPrefs.getUserId(applicationContext)).
                            child(Constants.FRIENDS).child(data.getKey()).removeValue();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void saveUserPrefs(UserPreferencesModel mPrefsModel, Context applicationContext) {
        userPrefsReference = FirebaseDatabase.getInstance().getReference().child(Constants.FIREBASE_CHILD_USERS).child(mPrefs.getUserId(applicationContext)).
                child(Constants.FIREBASE_CHILD_USER_PREFS);
        userPrefsReference.setValue(mPrefsModel);
    }

    public void saveUpcomingVerseToFirebase(ScriptureData verse, Context context){
        upcomingVersesReference = FirebaseDatabase.getInstance().getReference().child(Constants.FIREBASE_CHILD_USERS).child(mPrefs.getUserId(context)).
                child(Constants.FIREBASE_CHILD_UPCOMING_VERSES);
        upcomingVersesReference.push().setValue(verse);
    }

    public void saveQuizVerseToFirebase(ScriptureData verse, Context context){
        quizVersesReference = FirebaseDatabase.getInstance().getReference().child(Constants.FIREBASE_CHILD_USERS).child(mPrefs.getUserId(context)).
                child(Constants.FIREBASE_CHILD_QUIZ_VERSES);
        quizVersesReference.push().setValue(verse);
    }

    public void saveMemorizedVerseToFirebase(ScriptureData verse, Context context){
        memorizedVersesReference = FirebaseDatabase.getInstance().getReference().child(Constants.FIREBASE_CHILD_USERS).child(mPrefs.getUserId(context)).
                child(Constants.FIREBASE_CHILD_MEMORIZED_VERSES);
        memorizedVersesReference.push().setValue(verse);
    }

    public void saveForgottenVerseToFirebase(ScriptureData verse, Context applicationContext) {
        forgottenVersesReference = FirebaseDatabase.getInstance().getReference().child(Constants.FIREBASE_CHILD_USERS).child(mPrefs.getUserId(applicationContext)).
                child(Constants.FIREBASE_CHILD_FORGOTTEN_VERSES);
        forgottenVersesReference.push().setValue(verse);
    }

    public void deleteUpcomingVerse(ScriptureData verse, final Context context) {
        upcomingVersesReference = FirebaseDatabase.getInstance().getReference().child(Constants.FIREBASE_CHILD_USERS).child(mPrefs.getUserId(context)).
                child(Constants.FIREBASE_CHILD_UPCOMING_VERSES);
        upcomingVersesReference.orderByChild("verseLocation").equalTo(verse.getVerseLocation()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot data : dataSnapshot.getChildren()) {
                    FirebaseDatabase.getInstance().getReference().child(Constants.FIREBASE_CHILD_USERS).child(mPrefs.getUserId(context)).
                            child(Constants.FIREBASE_CHILD_UPCOMING_VERSES).child(data.getKey()).removeValue();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void deleteQuizVerse(ScriptureData verse, final Context context) {
        quizVersesReference = FirebaseDatabase.getInstance().getReference().child(Constants.FIREBASE_CHILD_USERS).child(mPrefs.getUserId(context)).
                child(Constants.FIREBASE_CHILD_QUIZ_VERSES);
        quizVersesReference.orderByChild("verseLocation").equalTo(verse.getVerseLocation()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot data : dataSnapshot.getChildren()) {
                    FirebaseDatabase.getInstance().getReference().child(Constants.FIREBASE_CHILD_USERS).child(mPrefs.getUserId(context)).
                            child(Constants.FIREBASE_CHILD_QUIZ_VERSES).child(data.getKey()).removeValue();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void deleteMemorizedVerse(ScriptureData verse, final Context context) {
        memorizedVersesReference = FirebaseDatabase.getInstance().getReference().child(Constants.FIREBASE_CHILD_USERS).child(mPrefs.getUserId(context)).
                child(Constants.FIREBASE_CHILD_MEMORIZED_VERSES);
        memorizedVersesReference.orderByChild("verseLocation").equalTo(verse.getVerseLocation()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot data : dataSnapshot.getChildren()) {
                    FirebaseDatabase.getInstance().getReference().child(Constants.FIREBASE_CHILD_USERS).child(mPrefs.getUserId(context)).
                            child(Constants.FIREBASE_CHILD_MEMORIZED_VERSES).child(data.getKey()).removeValue();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void deleteForgottenVerse(ScriptureData verse, final Context context) {
        forgottenVersesReference = FirebaseDatabase.getInstance().getReference().child(Constants.FIREBASE_CHILD_USERS).child(mPrefs.getUserId(context)).
                child(Constants.FIREBASE_CHILD_FORGOTTEN_VERSES);
        forgottenVersesReference.orderByChild("verseLocation").equalTo(verse.getVerseLocation()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot data : dataSnapshot.getChildren()) {
                    FirebaseDatabase.getInstance().getReference().child(Constants.FIREBASE_CHILD_USERS).child(mPrefs.getUserId(context)).
                            child(Constants.FIREBASE_CHILD_FORGOTTEN_VERSES).child(data.getKey()).removeValue();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void getUpcomingVersesFromFirebaseDb(Context context, final BaseCallback<List<ScriptureData>> upcomingCallback){
        final List<ScriptureData> upcomingList = new ArrayList<>();
        upcomingVersesReference = FirebaseDatabase.getInstance().getReference().child(Constants.FIREBASE_CHILD_USERS).child(mPrefs.getUserId(context)).
                child(Constants.FIREBASE_CHILD_UPCOMING_VERSES);
        upcomingVersesReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot data : dataSnapshot.getChildren()) {
                    ScriptureData verse = data.getValue(ScriptureData.class);
                    upcomingList.add(verse);
                }
                upcomingCallback.onResponse(upcomingList);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                upcomingList.add(new ScriptureData());
            }
        });
    }

    public void getQuizVersesFromFirebaseDb(Context applicationContext, final BaseCallback<List<ScriptureData>> quizCallback) {
        final List<ScriptureData> quizList = new ArrayList<>();
        quizVersesReference = FirebaseDatabase.getInstance().getReference().child(Constants.FIREBASE_CHILD_USERS).child(mPrefs.getUserId(applicationContext)).
                child(Constants.FIREBASE_CHILD_QUIZ_VERSES);
        quizVersesReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot data : dataSnapshot.getChildren()) {
                    ScriptureData verse = data.getValue(ScriptureData.class);
                    quizList.add(verse);
                }
                quizCallback.onResponse(quizList);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                quizList.add(new ScriptureData());
            }
        });
    }

    public void getMemorizedVersesFromFirebaseDb(Context applicationContext, final BaseCallback<List<ScriptureData>> memorizedCallback) {
        final List<ScriptureData> memorizedList = new ArrayList<>();
        memorizedVersesReference = FirebaseDatabase.getInstance().getReference().child(Constants.FIREBASE_CHILD_USERS).child(mPrefs.getUserId(applicationContext)).
                child(Constants.FIREBASE_CHILD_MEMORIZED_VERSES);
        memorizedVersesReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot data : dataSnapshot.getChildren()) {
                    ScriptureData verse = data.getValue(ScriptureData.class);
                    memorizedList.add(verse);
                }
                memorizedCallback.onResponse(memorizedList);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                memorizedList.add(new ScriptureData());
            }
        });
    }

    public void getForgottenVersesFromFirebaseDb(Context applicationContext, final BaseCallback<List<ScriptureData>> forgottenCallback) {
        final List<ScriptureData> forgottenList = new ArrayList<>();
        forgottenVersesReference = FirebaseDatabase.getInstance().getReference().child(Constants.FIREBASE_CHILD_USERS).child(mPrefs.getUserId(applicationContext)).
                child(Constants.FIREBASE_CHILD_FORGOTTEN_VERSES);
        forgottenVersesReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot data : dataSnapshot.getChildren()) {
                    ScriptureData verse = data.getValue(ScriptureData.class);
                    forgottenList.add(verse);
                }
                forgottenCallback.onResponse(forgottenList);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                forgottenList.add(new ScriptureData());
            }
        });
    }

    public void getUserPrefsFromFirebaseDb(Context context, final BaseCallback<UserPreferencesModel> userPrefsCallback){
        userPrefsReference = FirebaseDatabase.getInstance().getReference().child(Constants.FIREBASE_CHILD_USERS).child(mPrefs.getUserId(context)).
                child(Constants.FIREBASE_CHILD_USER_PREFS);
        userPrefsReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.getValue() != null) {
                    userPrefsCallback.onResponse(dataSnapshot.getValue(UserPreferencesModel.class));
                }else{
                    userPrefsCallback.onFailure(new Exception("data does not exist"));
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                userPrefsCallback.onResponse(null);
            }
        });
    }

    public void updateQuizVerse(final ScriptureData verse, final Context context){
        quizVersesReference = FirebaseDatabase.getInstance().getReference().child(Constants.FIREBASE_CHILD_USERS).child(mPrefs.getUserId(context)).
                child(Constants.FIREBASE_CHILD_QUIZ_VERSES);
        quizVersesReference.orderByChild("verseLocation").equalTo(verse.getVerseLocation()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot data : dataSnapshot.getChildren()) {
                    HashMap<String, Object> result = new HashMap<>();
                    result.put("verseLocation", verse.getVerseLocation());
                    result.put("rememberedDate", verse.getRemeberedDate());
                    result.put("lastSeenDate", verse.getLastSeenDate());
                    result.put("correctCount", verse.getCorrectCount());
                    result.put("viewedCount", verse.getViewedCount());
                    result.put("memoryStage", verse.getMemoryStage());
                    result.put("memorySubStage", verse.getMemorySubStage());
                    result.put("versionCode", verse.getVersionCode());
                    FirebaseDatabase.getInstance().getReference().child(Constants.FIREBASE_CHILD_USERS).child(mPrefs.getUserId(context)).
                            child(Constants.FIREBASE_CHILD_QUIZ_VERSES).child(data.getKey()).updateChildren(result);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void updateForgottenVerse(final ScriptureData verse, final Context context){
        forgottenVersesReference = FirebaseDatabase.getInstance().getReference().child(Constants.FIREBASE_CHILD_USERS).child(mPrefs.getUserId(context)).
                child(Constants.FIREBASE_CHILD_FORGOTTEN_VERSES);
        forgottenVersesReference.orderByChild("verseLocation").equalTo(verse.getVerseLocation()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot data : dataSnapshot.getChildren()) {
                    HashMap<String, Object> result = new HashMap<>();
                    result.put("verseLocation", verse.getVerseLocation());
                    result.put("rememberedDate", verse.getRemeberedDate());
                    result.put("lastSeenDate", verse.getLastSeenDate());
                    result.put("correctCount", verse.getCorrectCount());
                    result.put("viewedCount", verse.getViewedCount());
                    result.put("memoryStage", verse.getMemoryStage());
                    result.put("memorySubStage", verse.getMemorySubStage());
                    result.put("versionCode", verse.getVersionCode());
                    FirebaseDatabase.getInstance().getReference().child(Constants.FIREBASE_CHILD_USERS).child(mPrefs.getUserId(context)).
                            child(Constants.FIREBASE_CHILD_FORGOTTEN_VERSES).child(data.getKey()).updateChildren(result);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
