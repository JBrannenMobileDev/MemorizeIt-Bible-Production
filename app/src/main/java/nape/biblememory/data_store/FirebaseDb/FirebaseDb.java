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
import nape.biblememory.Models.ScriptureData;
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

    private DatabaseReference userReference;
    private DatabaseReference upcomingVersesReference;
    private DatabaseReference quizVersesReference;
    private DatabaseReference memorizedVersesReference;
    private DatabaseReference userPrefsReference;

    private UserPreferences mPrefs;

    private FirebaseDb() {
        mPrefs = new UserPreferences();
    }

    public void saveUser(User user, Context context){
        userReference = FirebaseDatabase.getInstance().getReference().child(Constants.FIREBASE_CHILD_USERRS);
        List<User> userList = getUsers(context);
        for(User userTemp : userList){
            if(userTemp.getUID().equalsIgnoreCase(user.getUID())){
                return;
            }
        }
        userReference.push().setValue(user);
    }

    private List<User> getUsers(Context context) {
        List<User> userList = new ArrayList<>();
        return userList;
    }

    public void saveUserPrefs(UserPreferencesModel mPrefsModel, Context applicationContext) {
        userPrefsReference = FirebaseDatabase.getInstance().getReference().child(mPrefs.getUserId(applicationContext)).child(Constants.FIREBASE_CHILD_USER_PREFS);
        userPrefsReference.setValue(mPrefsModel);
    }

    public void saveUpcomingVerseToFirebase(ScriptureData verse, Context context){
        upcomingVersesReference = FirebaseDatabase.getInstance().getReference().child(mPrefs.getUserId(context)).child(Constants.FIREBASE_CHILD_UPCOMING_VERSES);
        upcomingVersesReference.push().setValue(verse);
    }

    public void saveQuizVerseToFirebase(ScriptureData verse, Context context){
        quizVersesReference = FirebaseDatabase.getInstance().getReference().child(mPrefs.getUserId(context)).child(Constants.FIREBASE_CHILD_QUIZ_VERSES);
        quizVersesReference.push().setValue(verse);
    }

    public void saveMemorizedVerseToFirebase(ScriptureData verse, Context context){
        memorizedVersesReference = FirebaseDatabase.getInstance().getReference().child(mPrefs.getUserId(context)).child(Constants.FIREBASE_CHILD_MEMORIZED_VERSES);
        memorizedVersesReference.push().setValue(verse);
    }

    public void deleteUpcomingVerse(ScriptureData verse, final Context context) {
        upcomingVersesReference = FirebaseDatabase.getInstance().getReference().child(mPrefs.getUserId(context)).child(Constants.FIREBASE_CHILD_UPCOMING_VERSES);
        upcomingVersesReference.orderByChild("verseLocation").equalTo(verse.getVerseLocation()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot data : dataSnapshot.getChildren()) {
                    FirebaseDatabase.getInstance().getReference().child(mPrefs.getUserId(context)).child(Constants.FIREBASE_CHILD_UPCOMING_VERSES).child(data.getKey()).removeValue();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void deleteQuizVerse(ScriptureData verse, final Context context) {
        quizVersesReference = FirebaseDatabase.getInstance().getReference().child(mPrefs.getUserId(context)).child(Constants.FIREBASE_CHILD_QUIZ_VERSES);
        quizVersesReference.orderByChild("verseLocation").equalTo(verse.getVerseLocation()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot data : dataSnapshot.getChildren()) {
                    FirebaseDatabase.getInstance().getReference().child(mPrefs.getUserId(context)).child(Constants.FIREBASE_CHILD_QUIZ_VERSES).child(data.getKey()).removeValue();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void deleteMemorizedVerse(ScriptureData verse, final Context context) {
        memorizedVersesReference = FirebaseDatabase.getInstance().getReference().child(mPrefs.getUserId(context)).child(Constants.FIREBASE_CHILD_MEMORIZED_VERSES);
        memorizedVersesReference.orderByChild("verseLocation").equalTo(verse.getVerseLocation()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot data : dataSnapshot.getChildren()) {
                    FirebaseDatabase.getInstance().getReference().child(mPrefs.getUserId(context)).child(Constants.FIREBASE_CHILD_MEMORIZED_VERSES).child(data.getKey()).removeValue();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void getUpcomingVersesFromFirebaseDb(Context context, final BaseCallback<List<ScriptureData>> upcomingCallback){
        final List<ScriptureData> upcomingList = new ArrayList<>();
        upcomingVersesReference = FirebaseDatabase.getInstance().getReference().child(mPrefs.getUserId(context)).child(Constants.FIREBASE_CHILD_UPCOMING_VERSES);
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
        quizVersesReference = FirebaseDatabase.getInstance().getReference().child(mPrefs.getUserId(applicationContext)).child(Constants.FIREBASE_CHILD_QUIZ_VERSES);
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
        memorizedVersesReference = FirebaseDatabase.getInstance().getReference().child(mPrefs.getUserId(applicationContext)).child(Constants.FIREBASE_CHILD_MEMORIZED_VERSES);
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

    public void getUserPrefsFromFirebaseDb(Context context, final BaseCallback<UserPreferencesModel> userPrefsCallback){
        userPrefsReference = FirebaseDatabase.getInstance().getReference().child(mPrefs.getUserId(context)).child(Constants.FIREBASE_CHILD_USER_PREFS);
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
        quizVersesReference = FirebaseDatabase.getInstance().getReference().child(mPrefs.getUserId(context)).child(Constants.FIREBASE_CHILD_QUIZ_VERSES);
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
                    FirebaseDatabase.getInstance().getReference().child(mPrefs.getUserId(context)).child(Constants.FIREBASE_CHILD_QUIZ_VERSES).child(data.getKey()).updateChildren(result);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
