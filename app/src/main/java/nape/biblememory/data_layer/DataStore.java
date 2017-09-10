package nape.biblememory.data_layer;

import android.content.Context;
import android.content.Intent;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.Sort;
import nape.biblememory.data_layer.realm_db.RealmManager;
import nape.biblememory.models.MyVerse;
import nape.biblememory.utils.UserPreferences;
import nape.biblememory.view_layer.activities.BaseCallback;
import nape.biblememory.models.ScriptureData;
import nape.biblememory.models.User;
import nape.biblememory.models.UserPreferencesModel;
import nape.biblememory.data_layer.firebase_db.FirebaseDb;
import nape.biblememory.view_layer.activities.MainActivity;

/**
 * Created by jbrannen on 8/7/17.
 */

public class DataStore {

    private RealmManager realmManager;

    private static final DataStore ourInstance = new DataStore();

    public static DataStore getInstance() {
        return ourInstance;
    }

    private List<BaseCallback> friendRequestsRegisteredCallbacks;

    private DataStore() {
        friendRequestsRegisteredCallbacks = new ArrayList<>();
        realmManager = new RealmManager();
    }

    public void updateRealm(final Context context) {
        BaseCallback<List<ScriptureData>> quizVersesCallback = new BaseCallback<List<ScriptureData>>() {
            @Override
            public void onResponse(List<ScriptureData> response) {
                realmManager.insertOrUpdateMyVersesWithScriptureData(response);
                Intent intent = new Intent(context, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                context.startActivity(intent);
            }

            @Override
            public void onFailure(Exception e) {

            }
        };
        BaseCallback<UserPreferencesModel> userPrefsCallback = new BaseCallback<UserPreferencesModel>() {
            @Override
            public void onResponse(UserPreferencesModel response) {
                if(response != null){
                    UserPreferences mPrefs = new UserPreferences();
                    mPrefs.setPrefs(response, context);
                }
            }

            @Override
            public void onFailure(Exception e) {

            }
        };
        FirebaseDb.getInstance().getQuizVersesFromFirebaseDb(context, quizVersesCallback);
        getUserPrefs(context, userPrefsCallback);
    }

    public void registerForFriendRequests(final BaseCallback<List<User>> requestsCallback){
        friendRequestsRegisteredCallbacks.add(requestsCallback);
    }

    public void unregisterForFriendRequests(final BaseCallback<List<User>> requestsCallback){
        friendRequestsRegisteredCallbacks.remove(requestsCallback);
    }

    public void addFriend(String uid, Context contex){
        FirebaseDb.getInstance().addFriend(uid, contex);
    }

    public void deleteFriend(String uid, Context context){
        FirebaseDb.getInstance().deleteFriend(uid, context);
    }

    public void unFollowFriend(String uid, Context context){
        FirebaseDb.getInstance().unFlollowFriend(uid, context);
    }

    public void getPendingRequests(BaseCallback<List<String>> pendingCallback, Context context){
        FirebaseDb.getInstance().getPendingRequests(pendingCallback, context);
    }

    public void addPendingRequest(String uid, Context context){
        FirebaseDb.getInstance().addPendingRequest(uid, context);
    }

    public void getFriendRequests(Context context){
        BaseCallback<List<String>> uidListCallback = new BaseCallback<List<String>>() {
            @Override
            public void onResponse(List<String> response) {
                BaseCallback<List<User>> userCallback = new BaseCallback<List<User>>() {
                    @Override
                    public void onResponse(List<User> response) {
                        if(response != null) {
                            if(friendRequestsRegisteredCallbacks != null && friendRequestsRegisteredCallbacks.size() > 0) {
                                for(BaseCallback callback : friendRequestsRegisteredCallbacks){
                                    callback.onResponse(response);
                                }
                            }
                        }
                    }

                    @Override
                    public void onFailure(Exception e) {

                    }
                };
                if(response != null)
                    FirebaseDb.getInstance().getUsers(response, userCallback);
            }

            @Override
            public void onFailure(Exception e) {

            }
        };
        FirebaseDb.getInstance().getFriendRequests(uidListCallback, context);
    }

    public void deleteFriendRequest(String uid, Context context){
        FirebaseDb.getInstance().deleteFriendRequest(uid, context);
    }

    public void confirmFriendRequest(String uid, Context context){
        FirebaseDb.getInstance().confirmFriendRequest(uid, context);
        FirebaseDb.getInstance().deleteFriendRequest(uid, context);
    }

    public void sendFriendRequest(String uid, Context context){
        FirebaseDb.getInstance().sendFriendRequest(uid, context);
    }

    public void getFriends(final BaseCallback<List<User>> usersCallback, Context context){
        BaseCallback<List<String>> uidListCallback = new BaseCallback<List<String>>() {
            @Override
            public void onResponse(List<String> response) {
                BaseCallback<List<User>> userCallback = new BaseCallback<List<User>>() {
                    @Override
                    public void onResponse(List<User> response) {
                        if(response != null)
                            usersCallback.onResponse(response);
                    }

                    @Override
                    public void onFailure(Exception e) {

                    }
                };
                if(response != null)
                    FirebaseDb.getInstance().getUsers(response, userCallback);
            }

            @Override
            public void onFailure(Exception e) {

            }
        };
        FirebaseDb.getInstance().getFriends(uidListCallback, context);
    }

    public void getFriendsString(final BaseCallback<List<String>> usersCallback, Context context){
        BaseCallback<List<String>> uidListCallback = new BaseCallback<List<String>>() {
            @Override
            public void onResponse(List<String> response) {
                usersCallback.onResponse(response);
            }

            @Override
            public void onFailure(Exception e) {

            }
        };
        FirebaseDb.getInstance().getFriends(uidListCallback, context);
    }

    public void getUsers(BaseCallback<List<User>> usersCallback){
        FirebaseDb.getInstance().getUsers(usersCallback);
    }

    public void getUsersThatBlessedMe(BaseCallback<List<User>> usersThatBlessedCallback, Context context){
        FirebaseDb.getInstance().getUsersThatBlessed(usersThatBlessedCallback, context);
    }

    public void addNewUser(User user){
        FirebaseDb.getInstance().addNewUser(user);
    }

    public void updateSingleVerseUserData(String UID, int updateAmount){
        FirebaseDb.getInstance().updateSingleVerseUserdata(UID, updateAmount);
    }

//    public void updateUserData(final String uid, final Context context){
//        BaseCallback<List<ScriptureData>> learningCallback = new BaseCallback<List<ScriptureData>>() {
//            int verseCount = 0;
//            @Override
//            public void onResponse(List<ScriptureData> response) {
//                if(response != null){
//                    verseCount = verseCount + response.size();
//                }
//                BaseCallback<List<ScriptureData>> memorizedCallback = new BaseCallback<List<ScriptureData>>() {
//                    @Override
//                    public void onResponse(List<ScriptureData> response) {
//                        if(response != null){
//                            verseCount = verseCount + response.size();
//                        }
//                        BaseCallback<List<ScriptureData>> forgottenCallback = new BaseCallback<List<ScriptureData>>() {
//                            @Override
//                            public void onResponse(List<ScriptureData> response) {
//                                if(response != null){
//                                    verseCount = verseCount + response.size();
//                                }
//                                FirebaseDb.getInstance().updateUserdata(uid, verseCount);
//                            }
//
//                            @Override
//                            public void onFailure(Exception e) {
//
//                            }
//                        };
//                        getForgottenVerses(forgottenCallback, context);
//                    }
//
//                    @Override
//                    public void onFailure(Exception e) {
//
//                    }
//                };
//                getMemorizedVerses(memorizedCallback, context);
//            }
//
//            @Override
//            public void onFailure(Exception e) {
//
//            }
//        };
//        getQuizVerses(learningCallback, context);
//    }

    public void saveQuizVerse(ScriptureData verse, Context applicationContext){
        FirebaseDb.getInstance().saveQuizVerseToFirebase(verse, applicationContext);
        realmManager.insertOrUpdateVerse(verse.toMyVerse());
    }

    public void saveMemorizedVerse(ScriptureData verse, Context applicationContext){
        FirebaseDb.getInstance().saveMemorizedVerseToFirebase(verse, applicationContext);
        //TODO add memorized verse support to Realm
    }

    public void saveForgottenVerse(ScriptureData verse, Context applicationContext){
        FirebaseDb.getInstance().saveForgottenVerseToFirebase(verse, applicationContext);
        //TODO add memorized verse support to Realm
    }

    public void deleteQuizVerse(MyVerse verse, Context context){
        FirebaseDb.getInstance().deleteQuizVerse(verse, context);
        realmManager.deleteQuizVerse(verse);
    }

    public void deleteMemorizedVerse(ScriptureData verse, Context context){
        //TODO add delete memorized support for Realm
       FirebaseDb.getInstance().deleteMemorizedVerse(verse, context);
    }

    public void deleteForgottenVerse(ScriptureData verse, Context context){
        //TODO add delete forgotten support for Realm
        FirebaseDb.getInstance().deleteForgottenVerse(verse, context);
    }

    public void getMemorizedVerses(BaseCallback<List<ScriptureData>> memorizedCallback, Context applicationContext){
        FirebaseDb.getInstance().getMemorizedVersesFromFirebaseDb(applicationContext, memorizedCallback);
        //TODO add memorized verse support to realm
    }

    public void getForgottenVerses(BaseCallback<List<ScriptureData>> forgottenCallback, Context applicationContext){
        FirebaseDb.getInstance().getForgottenVersesFromFirebaseDb(applicationContext, forgottenCallback);
        //TODO add memorized verse support to realm
    }

    public void updateQuizVerse(final MyVerse verse, Context applcationContext){
        FirebaseDb.getInstance().updateQuizVerse(verse, applcationContext);
        Realm realm = Realm.getDefaultInstance();
        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                MyVerse realmVerse = realm.where(MyVerse.class).equalTo("verseLocation", verse.getVerseLocation()).findFirst();
                realmVerse.setMemoryStage(verse.getMemoryStage());
                realmVerse.setMemorySubStage(verse.getMemorySubStage());
                realmVerse.setCorrectCount(verse.getCorrectCount());
                realmVerse.setLastSeenDate(verse.getLastSeenDate());
                realm.copyToRealmOrUpdate(realmVerse);
            }
        });
        realm.close();
    }

    public void updateQuizVerse(final MyVerse locationToUpdate, final MyVerse valueToSave, Context applcationContext){
        FirebaseDb.getInstance().updateQuizVerse(locationToUpdate, valueToSave, applcationContext);
    }

    public void updateQuizVerseToRealm(final List<MyVerse> quizVerses){
        Realm realm = Realm.getDefaultInstance();
        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                for(MyVerse verse : quizVerses){
                    MyVerse realmVerse = realm.where(MyVerse.class).equalTo("verseLocation", verse.getVerseLocation()).findFirst();
                    if(realmVerse != null) {
                        realmVerse.setListPosition(verse.getListPosition());
                        realmVerse.setGoldStar(verse.getGoldStar());
                        realm.copyToRealmOrUpdate(realmVerse);
                    }
                }
            }
        });
        realm.close();
    }

    public void updateForgottenVerse(ScriptureData verse, Context applcationContext){
        FirebaseDb.getInstance().updateForgottenVerse(verse, applcationContext);
    }

    public void getRandomQuizVerse(final BaseCallback<MyVerse> verseCallback){
        Realm realm = Realm.getDefaultInstance();
        RealmResults<MyVerse> quizList = realm.where(MyVerse.class).findAll().sort("listPosition", Sort.ASCENDING);
        realm.close();

        int caseNumber;
        MyVerse resultVerse = null;

        if(quizList != null && quizList.size() > 2 && quizList.get(0).getVerse() != null && quizList.get(1).getVerse() != null && quizList.get(2).getVerse() != null) {
            Random generate = new Random();
            final int random = generate.nextInt(100) + 1;
            caseNumber = calculateCaseNumber(random);
            switch (caseNumber) {
                case 1:
                    resultVerse = quizList.get(0);
                    break;
                case 2:
                    resultVerse = quizList.get(1);
                    break;
                case 3:
                    resultVerse = quizList.get(2);
                    break;
                default:
                    resultVerse = quizList.get(0);
            }
        }else if(quizList != null && quizList.size() == 2 && quizList.get(0).getVerse() != null && quizList.get(1).getVerse() != null){
            Random generate = new Random();
            final int random = generate.nextInt(100) + 1;
            caseNumber = calculateCaseNumber(random);
            switch (caseNumber) {
                case 1:
                    resultVerse = quizList.get(0);
                    break;
                case 2:
                case 3:
                    resultVerse = quizList.get(1);
                    break;
                default:
                    resultVerse = quizList.get(0);
            }
        }else if(quizList != null && quizList.size() == 1 && quizList.get(0).getVerse() != null){
            Random generate = new Random();
            final int random = generate.nextInt(100) + 1;
            caseNumber = calculateCaseNumber(random);
            switch (caseNumber) {
                case 1:
                case 2:
                case 3:
                    resultVerse = quizList.get(0);
                    break;
                default:
                    resultVerse = quizList.get(0);
            }
        }
        verseCallback.onResponse(resultVerse);
    }

    private int calculateCaseNumber(int random) {
        if(random > 66){
            return 1;
        }else if(random > 33){
            return 2;
        }else {
            return 3;
        }
    }

    public void saveUserPrefs(UserPreferencesModel mPrefsModel, Context applicationContext) {
        FirebaseDb.getInstance().saveUserPrefs(mPrefsModel, applicationContext);
    }

    public void getUserPrefs(Context context, BaseCallback<UserPreferencesModel> userPrefsCallback){
        FirebaseDb.getInstance().getUserPrefsFromFirebaseDb(context, userPrefsCallback);
    }

    public void getAllVerses(final String uid, final BaseCallback<List<ScriptureData>> allVersesCallback) {
        BaseCallback<List<ScriptureData>> memorizedCallback = new BaseCallback<List<ScriptureData>>() {
            Realm realm = Realm.getDefaultInstance();
            RealmResults<MyVerse> quizList = realm.where(MyVerse.class).findAll();
            final List<ScriptureData> allVerses = new ArrayList<>();
            @Override
            public void onResponse(List<ScriptureData> response) {
                for(MyVerse verse : quizList){
                    allVerses.add(verse.toScriptureData());
                }
                if(response != null){
                    allVerses.addAll(response);
                }
                BaseCallback<List<ScriptureData>> forgottenCallback = new BaseCallback<List<ScriptureData>>() {
                    @Override
                    public void onResponse(List<ScriptureData> response) {
                        if(response != null){
                            allVerses.addAll(response);
                        }
                        allVersesCallback.onResponse(allVerses);
                    }

                    @Override
                    public void onFailure(Exception e) {

                    }
                };
                FirebaseDb.getInstance().getForgottenVersesFromFirebaseDb(forgottenCallback, uid);
            }

            @Override
            public void onFailure(Exception e) {

            }
        };
        FirebaseDb.getInstance().getMemorizedVersesFromFirebaseDb(memorizedCallback, uid);
    }

    public void nukeAllData(Context context) {
        FirebaseDb.getInstance().nukeDb(context);
    }

    public void sendABlessing(String userId, Context applicationContext) {
        FirebaseDb.getInstance().sendBlessing(userId, applicationContext);
    }

    public void deleteFriendBlessing(String uidToDelete, Context applicationContext) {
        FirebaseDb.getInstance().deleteBlessingNotification(uidToDelete, applicationContext);
    }

    public void addVerseMemorized(ScriptureData verse){
        FirebaseDb.getInstance().addVerseMemorized(verse);
    }

    public void getAllMemorizedVerses(final BaseCallback<List<ScriptureData>> allMemorizedCallback){
        FirebaseDb.getInstance().getAllMemorizedVerses(allMemorizedCallback);
    }
}
