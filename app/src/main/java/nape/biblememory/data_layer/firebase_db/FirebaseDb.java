package nape.biblememory.data_layer.firebase_db;

import android.content.Context;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import nape.biblememory.models.MyVerse;
import nape.biblememory.models.User;
import nape.biblememory.view_layer.activities.BaseCallback;
import nape.biblememory.models.ScriptureData;
import nape.biblememory.models.UserPreferencesModel;
import nape.biblememory.utils.UserPreferences;

/**
 * Created by jbrannen on 8/7/17.
 */

public class FirebaseDb {
    private static final FirebaseDb ourInstance = new FirebaseDb();
    public static FirebaseDb getInstance() {
        return ourInstance;
    }

    private DatabaseReference friendsReference;
    private DatabaseReference friendsRequestReference;
    private DatabaseReference usersReference;
    private DatabaseReference upcomingVersesReference;
    private DatabaseReference quizVersesReference;
    private DatabaseReference memorizedVersesReference;
    private DatabaseReference forgottenVersesReference;
    private DatabaseReference userPrefsReference;
    private DatabaseReference blessingReference;
    private DatabaseReference allMemorizedReference;

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

    public void getUsersThatBlessed(final BaseCallback<List<User>> userDataCallback, Context context) {
        final BaseCallback<List<String>> uidBlessed = new BaseCallback<List<String>>() {
            @Override
            public void onResponse(final List<String> response) {
                if(response != null) {
                    final List<User> users = new ArrayList<>();
                    usersReference = FirebaseDatabase.getInstance().getReference().child(Constants.FIREBASE_CHILD_USER_DATA);
                    usersReference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for (DataSnapshot data : dataSnapshot.getChildren()) {
                                User user = data.getValue(User.class);
                                if (response.contains(user.getUID())) {
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
            }

            @Override
            public void onFailure(Exception e) {

            }
        };
        getBlessings(uidBlessed, context);
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
        usersReference.orderByChild("uid").equalTo(UID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot data : dataSnapshot.getChildren()) {
                    User user = data.getValue(User.class);
                    HashMap<String, Object> result = new HashMap<>();
                    result.put("totalVerses", updateAmount);
                    FirebaseDatabase.getInstance().getReference().child(Constants.FIREBASE_CHILD_USER_DATA)
                            .child(data.getKey()).updateChildren(result);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void updateSingleVerseUserdata(String UID, final int updateAmount){
        usersReference = FirebaseDatabase.getInstance().getReference().child(Constants.FIREBASE_CHILD_USER_DATA);
        usersReference.orderByChild("uid").equalTo(UID).addListenerForSingleValueEvent(new ValueEventListener() {
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

    public void addPendingRequest(String uid, Context applicationContext){
        friendsReference = FirebaseDatabase.getInstance().getReference().child(Constants.FIREBASE_CHILD_USERS).
                child(mPrefs.getUserId(applicationContext)).child(Constants.PENDING_REQUESTS);
        friendsReference.push().setValue(uid);
    }

    public void sendBlessing(String uidToSendTo, Context context){
        blessingReference = FirebaseDatabase.getInstance().getReference().child(Constants.FIREBASE_CHILD_USERS).
                child(uidToSendTo).child(Constants.BLESSINGS);
        blessingReference.push().setValue(mPrefs.getUserId(context));
    }

    public void getBlessings(final BaseCallback<List<String>> blessingCallback, Context context){
        blessingReference = FirebaseDatabase.getInstance().getReference().child(Constants.FIREBASE_CHILD_USERS).
                child(mPrefs.getUserId(context)).child(Constants.BLESSINGS);
        blessingReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<String> blessingList = new ArrayList<>();
                for(DataSnapshot data : dataSnapshot.getChildren()){
                    String blessing = data.getValue(String.class);
                    blessingList.add(blessing);
                }
                blessingCallback.onResponse(blessingList);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void getPendingRequests(final BaseCallback<List<String>> friendsCallback, Context context){
        friendsReference = FirebaseDatabase.getInstance().getReference().child(Constants.FIREBASE_CHILD_USERS).
                child(mPrefs.getUserId(context)).child(Constants.PENDING_REQUESTS);
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

    public void deletePendingRequest(final String uid, final Context context){
        friendsRequestReference = FirebaseDatabase.getInstance().getReference().child(Constants.FIREBASE_CHILD_USERS).
                child(uid).child(Constants.PENDING_REQUESTS);

        friendsRequestReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot data : dataSnapshot.getChildren()) {
                    if(data.getValue().equals(mPrefs.getUserId(context))){
                        FirebaseDatabase.getInstance().getReference().child(Constants.FIREBASE_CHILD_USERS).child(uid).
                                child(Constants.PENDING_REQUESTS).child(data.getKey()).removeValue();
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void deleteBlessingNotification(final String uidToRemove, final Context context){
        blessingReference = FirebaseDatabase.getInstance().getReference().child(Constants.FIREBASE_CHILD_USERS).
                child(mPrefs.getUserId(context)).child(Constants.BLESSINGS);

        blessingReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot data : dataSnapshot.getChildren()) {
                    if(data.getValue().equals(uidToRemove)){
                        FirebaseDatabase.getInstance().getReference().child(Constants.FIREBASE_CHILD_USERS).child(mPrefs.getUserId(context)).
                                child(Constants.BLESSINGS).child(data.getKey()).removeValue();
                    }
                }
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

    public void confirmFriendRequest(String requesterUid, Context context){
        addFriend(requesterUid, context);
        friendsReference = FirebaseDatabase.getInstance().getReference().child(Constants.FIREBASE_CHILD_USERS).
                child(requesterUid).child(Constants.FRIENDS);
        friendsReference.push().setValue(mPrefs.getUserId(context));
    }

    public void sendFriendRequest(String uid, Context context){
        friendsRequestReference = FirebaseDatabase.getInstance().getReference().child(Constants.FIREBASE_CHILD_USERS).
                child(uid).child(Constants.FRIEND_REQUESTS);
        friendsRequestReference.push().setValue(mPrefs.getUserId(context));
    }

    public void deleteFriendRequest(final String uid, final Context context){
        friendsRequestReference = FirebaseDatabase.getInstance().getReference().child(Constants.FIREBASE_CHILD_USERS).
                child(mPrefs.getUserId(context)).child(Constants.FRIEND_REQUESTS);

        friendsRequestReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot data : dataSnapshot.getChildren()) {
                    if(uid.equals(data.getValue())){
                        FirebaseDatabase.getInstance().getReference().child(Constants.FIREBASE_CHILD_USERS).child(mPrefs.getUserId(context)).
                                child(Constants.FRIEND_REQUESTS).child(data.getKey()).removeValue();
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        deletePendingRequest(uid, context);
    }

    public void getFriendRequests(final BaseCallback<List<String>> friendRequestCallback, Context context){
        friendsRequestReference = FirebaseDatabase.getInstance().getReference().child(Constants.FIREBASE_CHILD_USERS).
                child(mPrefs.getUserId(context)).child(Constants.FRIEND_REQUESTS);
        friendsRequestReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<String> uidList = new ArrayList<>();
                for(DataSnapshot data : dataSnapshot.getChildren()) {
                    String uid = data.getValue(String.class);
                    uidList.add(uid);
                }
                friendRequestCallback.onResponse(uidList);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void deleteFriend(final String friendUid, final Context applicationContext){
        friendsReference = FirebaseDatabase.getInstance().getReference().child(Constants.FIREBASE_CHILD_USERS).
                child(mPrefs.getUserId(applicationContext)).child(Constants.FRIENDS);
        friendsReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot data : dataSnapshot.getChildren()) {
                    if(friendUid.equals(data.getValue())){
                        FirebaseDatabase.getInstance().getReference().child(Constants.FIREBASE_CHILD_USERS).child(mPrefs.getUserId(applicationContext)).
                                child(Constants.FRIENDS).child(data.getKey()).removeValue();
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void deleteFriend(final String friendUid, final String myUid){
        friendsReference = FirebaseDatabase.getInstance().getReference().child(Constants.FIREBASE_CHILD_USERS).
                child(myUid).child(Constants.FRIENDS);
        friendsReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot data : dataSnapshot.getChildren()) {
                    if(friendUid.equals(data.getValue())){
                        FirebaseDatabase.getInstance().getReference().child(Constants.FIREBASE_CHILD_USERS).child(myUid).
                                child(Constants.FRIENDS).child(data.getKey()).removeValue();
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void deleteMeFromFriend(final String friendUid, final String myUid){
        friendsReference = FirebaseDatabase.getInstance().getReference().child(Constants.FIREBASE_CHILD_USERS).
                child(friendUid).child(Constants.FRIENDS);
        friendsReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot data : dataSnapshot.getChildren()) {
                    if(myUid.equals(data.getValue())){
                        FirebaseDatabase.getInstance().getReference().child(Constants.FIREBASE_CHILD_USERS).child(friendUid).
                                child(Constants.FRIENDS).child(data.getKey()).removeValue();
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void unFlollowFriend(String uid, Context context) {
        deleteFriend(uid, mPrefs.getUserId(context));
        deleteMeFromFriend(uid, mPrefs.getUserId(context));
    }

    public void saveUserPrefs(UserPreferencesModel mPrefsModel, Context applicationContext) {
        userPrefsReference = FirebaseDatabase.getInstance().getReference().child(Constants.FIREBASE_CHILD_USERS).child(mPrefs.getUserId(applicationContext)).
                child(Constants.FIREBASE_CHILD_USER_PREFS);
        userPrefsReference.setValue(mPrefsModel);
    }

    public void addVerseMemorized(ScriptureData verse){
        upcomingVersesReference = FirebaseDatabase.getInstance().getReference().child(Constants.ALL_MEMORIZED_VERSES);
        upcomingVersesReference.push().setValue(verse);
    }

    public void getAllMemorizedVerses(final BaseCallback<List<ScriptureData>> allMemorizedCallback){
        final List<ScriptureData> memorizedList = new ArrayList<>();
        allMemorizedReference = FirebaseDatabase.getInstance().getReference().child(Constants.ALL_MEMORIZED_VERSES);
        allMemorizedReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot data : dataSnapshot.getChildren()) {
                    ScriptureData verse = data.getValue(ScriptureData.class);
                    memorizedList.add(verse);
                }
                allMemorizedCallback.onResponse(memorizedList);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                memorizedList.add(new ScriptureData());
            }
        });
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

    public void deleteQuizVerse(MyVerse verse, final Context context) {
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
                Collections.sort(quizList);
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

    public void getMemorizedVersesFromFirebaseDb(final BaseCallback<List<ScriptureData>> memorizedCallback, String uid) {
        final List<ScriptureData> memorizedList = new ArrayList<>();
        memorizedVersesReference = FirebaseDatabase.getInstance().getReference().child(Constants.FIREBASE_CHILD_USERS).child(uid).
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
    public void getForgottenVersesFromFirebaseDb(final BaseCallback<List<ScriptureData>> forgottenCallback, String uid) {
        final List<ScriptureData> forgottenList = new ArrayList<>();
        forgottenVersesReference = FirebaseDatabase.getInstance().getReference().child(Constants.FIREBASE_CHILD_USERS).child(uid).
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

    public void updateQuizVerse(final MyVerse verse, final Context context){
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
                    result.put("viewedCount", verse.getViewedCount());
                    result.put("correctCount", verse.getCorrectCount());
                    result.put("goldStar", verse.getGoldStar());
                    FirebaseDatabase.getInstance().getReference().child(Constants.FIREBASE_CHILD_USERS).child(mPrefs.getUserId(context)).
                            child(Constants.FIREBASE_CHILD_QUIZ_VERSES).child(data.getKey()).updateChildren(result);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void updateQuizVerse(final MyVerse locationToUpdate, final MyVerse valueToSave, final Context context){
        quizVersesReference = FirebaseDatabase.getInstance().getReference().child(Constants.FIREBASE_CHILD_USERS).child(mPrefs.getUserId(context)).
                child(Constants.FIREBASE_CHILD_QUIZ_VERSES);
        quizVersesReference.orderByChild("verseLocation").equalTo(locationToUpdate.getVerseLocation()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot data : dataSnapshot.getChildren()) {
                    HashMap<String, Object> result = new HashMap<>();
                    result.put("bookName", valueToSave.getBookName());
                    result.put("verse", valueToSave.getVerse());
                    result.put("verseLocation", valueToSave.getVerseLocation());
                    result.put("rememberedDate", valueToSave.getRemeberedDate());
                    result.put("lastSeenDate", valueToSave.getLastSeenDate());
                    result.put("correctCount", valueToSave.getCorrectCount());
                    result.put("viewedCount", valueToSave.getViewedCount());
                    result.put("memoryStage", valueToSave.getMemoryStage());
                    result.put("memorySubStage", valueToSave.getMemorySubStage());
                    result.put("versionCode", valueToSave.getVersionCode());
                    result.put("viewedCount", valueToSave.getViewedCount());
                    result.put("correctCount", valueToSave.getCorrectCount());
                    result.put("listPosition", valueToSave.getListPosition());
                    result.put("goldStar", valueToSave.getGoldStar());
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
                    result.put("viewedCount", verse.getViewedCount());
                    result.put("correctCount", verse.getCorrectCount());
                    FirebaseDatabase.getInstance().getReference().child(Constants.FIREBASE_CHILD_USERS).child(mPrefs.getUserId(context)).
                            child(Constants.FIREBASE_CHILD_FORGOTTEN_VERSES).child(data.getKey()).updateChildren(result);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void nukeDb(Context context) {
        FirebaseDatabase.getInstance().getReference().child(Constants.FIREBASE_CHILD_USERS).child(mPrefs.getUserId(context)).removeValue();
        FirebaseDatabase.getInstance().getReference().child(Constants.FIREBASE_CHILD_USER_DATA).child(mPrefs.getUserId(context)).removeValue();
    }
}
