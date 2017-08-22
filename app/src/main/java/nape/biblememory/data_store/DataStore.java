package nape.biblememory.data_store;

import android.content.Context;
import android.content.Intent;

import java.util.List;
import java.util.Random;

import nape.biblememory.Activities.BaseCallback;
import nape.biblememory.Activities.MainActivity;
import nape.biblememory.Managers.NetworkManager;
import nape.biblememory.Managers.ScriptureManager;
import nape.biblememory.Managers.VerseOperations;
import nape.biblememory.Models.ScriptureData;
import nape.biblememory.Models.User;
import nape.biblememory.Models.UserPreferencesModel;
import nape.biblememory.UserPreferences;
import nape.biblememory.data_store.FirebaseDb.FirebaseDb;
import nape.biblememory.data_store.Sqlite.MemoryListContract;

/**
 * Created by jbrannen on 8/7/17.
 */

public class DataStore {
    private static final DataStore ourInstance = new DataStore();

    public static DataStore getInstance() {
        return ourInstance;
    }

    private ScriptureManager scriptureManager;

    private DataStore() {
    }

    public void getUsers(Context application, BaseCallback<List<User>> usersCallback){
        FirebaseDb.getInstance().getUsers(application, usersCallback);
    }

    public void addNewUser(User user){
        FirebaseDb.getInstance().addNewUser(user);
    }

    public void updateUserData(String UID, int updateAmount){
        FirebaseDb.getInstance().updateUserdata(UID, updateAmount);
    }

    public void saveUpcomingVerse(ScriptureData verse, Context applicationContext){
        FirebaseDb.getInstance().saveUpcomingVerseToFirebase(verse, applicationContext);
        VerseOperations.getInstance(applicationContext).addVerse(verse, MemoryListContract.CurrentSetEntry.TABLE_NAME);
    }

    public void saveQuizVerse(ScriptureData verse, Context applicationContext){
        FirebaseDb.getInstance().saveQuizVerseToFirebase(verse, applicationContext);
        VerseOperations.getInstance(applicationContext).addVerse(verse, MemoryListContract.LearningSetEntry.TABLE_NAME);
    }

    public void saveMemorizedVerse(ScriptureData verse, Context applicationContext){
        FirebaseDb.getInstance().saveMemorizedVerseToFirebase(verse, applicationContext);
        VerseOperations.getInstance(applicationContext).addVerse(verse, MemoryListContract.MemorizedSetEntry.TABLE_NAME);
    }

    public void saveForgottenVerse(ScriptureData verse, Context applicationContext){
        FirebaseDb.getInstance().saveForgottenVerseToFirebase(verse, applicationContext);
        VerseOperations.getInstance(applicationContext).addVerse(verse, MemoryListContract.ForgottenSetEntry.TABLE_NAME);
    }

    public void deleteUpcomingVerse(ScriptureData verse, Context context){
        FirebaseDb.getInstance().deleteUpcomingVerse(verse, context);
        VerseOperations.getInstance(context).removeVerse(verse.getVerseLocation(), MemoryListContract.CurrentSetEntry.TABLE_NAME);
    }

    public void deleteQuizVerse(ScriptureData verse, Context context){
        FirebaseDb.getInstance().deleteQuizVerse(verse, context);
        VerseOperations.getInstance(context).removeVerse(verse.getVerseLocation(), MemoryListContract.LearningSetEntry.TABLE_NAME);
    }

    public void deleteMemorizedVerse(ScriptureData verse, Context context){
        FirebaseDb.getInstance().deleteMemorizedVerse(verse, context);
        VerseOperations.getInstance(context).removeVerse(verse.getVerseLocation(), MemoryListContract.MemorizedSetEntry.TABLE_NAME);
    }

    public void deleteForgottenVerse(ScriptureData verse, Context context){
        FirebaseDb.getInstance().deleteForgottenVerse(verse, context);
        VerseOperations.getInstance(context).removeVerse(verse.getVerseLocation(), MemoryListContract.ForgottenSetEntry.TABLE_NAME);
    }

    public void getUpcomingVerses(BaseCallback<List<ScriptureData>> upcomingCallback, Context applicationContext){
        if(NetworkManager.getInstance().isInternet(applicationContext)){
            FirebaseDb.getInstance().getUpcomingVersesFromFirebaseDb(applicationContext, upcomingCallback);
        }else{
            if(scriptureManager == null) {
                scriptureManager = new ScriptureManager(applicationContext);
            }
            upcomingCallback.onResponse(scriptureManager.getScriptureSet(MemoryListContract.CurrentSetEntry.TABLE_NAME));
        }
    }

    public void getQuizVerses(BaseCallback<List<ScriptureData>> quizCallback, Context applicationContext){
        if(NetworkManager.getInstance().isInternet(applicationContext)){
            FirebaseDb.getInstance().getQuizVersesFromFirebaseDb(applicationContext, quizCallback);
        }else{
            if(scriptureManager == null) {
                scriptureManager = new ScriptureManager(applicationContext);
            }
            quizCallback.onResponse(scriptureManager.getScriptureSet(MemoryListContract.LearningSetEntry.TABLE_NAME));
        }
    }

    public void getMemorizedVerses(BaseCallback<List<ScriptureData>> memorizedCallback, Context applicationContext){
        if(NetworkManager.getInstance().isInternet(applicationContext)){
            FirebaseDb.getInstance().getMemorizedVersesFromFirebaseDb(applicationContext, memorizedCallback);
        }else{
            if(scriptureManager == null) {
                scriptureManager = new ScriptureManager(applicationContext);
            }
            memorizedCallback.onResponse(scriptureManager.getScriptureSet(MemoryListContract.MemorizedSetEntry.TABLE_NAME));
        }
    }

    public void getForgottenVerses(BaseCallback<List<ScriptureData>> forgottenCallback, Context applicationContext){
        if(NetworkManager.getInstance().isInternet(applicationContext)){
            FirebaseDb.getInstance().getForgottenVersesFromFirebaseDb(applicationContext, forgottenCallback);
        }else{
            if(scriptureManager == null) {
                scriptureManager = new ScriptureManager(applicationContext);
            }
            forgottenCallback.onResponse(scriptureManager.getScriptureSet(MemoryListContract.ForgottenSetEntry.TABLE_NAME));
        }
    }

    public void getLocalUpcomingVerses(BaseCallback<List<ScriptureData>> upcomingCallback, Context applicationContext){
        if(scriptureManager == null) {
            scriptureManager = new ScriptureManager(applicationContext);
        }
        upcomingCallback.onResponse(scriptureManager.getScriptureSet(MemoryListContract.CurrentSetEntry.TABLE_NAME));
    }

    public void getLocalQuizVerses(BaseCallback<List<ScriptureData>> quizCallback, Context applicationContext){
        if(scriptureManager == null) {
            scriptureManager = new ScriptureManager(applicationContext);
        }
        quizCallback.onResponse(scriptureManager.getScriptureSet(MemoryListContract.LearningSetEntry.TABLE_NAME));
    }

    public void getLocalForgottenVerse(final BaseCallback<ScriptureData> forgottenCallback, Context context){
        if(scriptureManager == null){
            scriptureManager = new ScriptureManager(context);
        }
        List<ScriptureData> forgottenList = scriptureManager.getScriptureSet(MemoryListContract.ForgottenSetEntry.TABLE_NAME);
        if(forgottenList.size() > 0) {
            forgottenCallback.onResponse(forgottenList.get(0));
        }else{
            forgottenCallback.onResponse(null);
        }
    }

    public void getLocalMemorizedVerses(BaseCallback<List<ScriptureData>> memorizedCallback, Context applicationContext){
        if(scriptureManager == null) {
            scriptureManager = new ScriptureManager(applicationContext);
        }
        memorizedCallback.onResponse(scriptureManager.getScriptureSet(MemoryListContract.MemorizedSetEntry.TABLE_NAME));
    }

    public void getLocalMeorizedVerseAt(final int index, final BaseCallback<ScriptureData> memorizedCallback, final Context context){
        final UserPreferences mPrefs = new UserPreferences();
        if(scriptureManager == null) {
            scriptureManager = new ScriptureManager(context);
        }
        BaseCallback<List<ScriptureData>> memorizedListCallback = new BaseCallback<List<ScriptureData>>() {
            @Override
            public void onResponse(List<ScriptureData> response) {
                int listSize = response.size();
                if(listSize > 0) {
                    if (index >= listSize) {
                        memorizedCallback.onResponse(response.get(0));
                        mPrefs.setQuizReviewIndex(0, context);
                    } else {
                        memorizedCallback.onResponse(response.get(index));
                    }
                }else{
                    memorizedCallback.onResponse(null);
                }
            }

            @Override
            public void onFailure(Exception e) {

            }
        };
        getLocalMemorizedVerses(memorizedListCallback, context);
    }

    public void updateQuizVerse(ScriptureData verse, Context applcationContext){
        if(scriptureManager == null){
            scriptureManager = new ScriptureManager(applcationContext);
        }
        scriptureManager.updateScriptureStatus(verse);
        FirebaseDb.getInstance().updateQuizVerse(verse, applcationContext);
    }

    public void moveUpcomingVerseToQuiz(final Context context){
        BaseCallback<List<ScriptureData>> upcomingCallback = new BaseCallback<List<ScriptureData>>() {
            @Override
            public void onResponse(List<ScriptureData> response) {
                if(response.size() > 0){
                    saveQuizVerse(response.get(0), context);
                    deleteUpcomingVerse(response.get(0), context);
                }
            }

            @Override
            public void onFailure(Exception e) {

            }
        };
        getLocalUpcomingVerses(upcomingCallback, context);
    }

    public void updateForgottenVerse(ScriptureData verse, Context applcationContext){
        if(scriptureManager == null){
            scriptureManager = new ScriptureManager(applcationContext);
        }
        scriptureManager.updateForgottenScriptureStatus(verse);
        FirebaseDb.getInstance().updateForgottenVerse(verse, applcationContext);
    }

    public void getRandomQuizVerse(Context applicationContext, final BaseCallback<ScriptureData> verseCallback){

        BaseCallback<List<ScriptureData>> quizVersesCallback = new BaseCallback<List<ScriptureData>>() {
            @Override
            public void onResponse(List<ScriptureData> response) {
                int caseNumber;
                List<ScriptureData> scriptureListLearning = response;
                ScriptureData resultVerse = null;

                if(scriptureListLearning != null && scriptureListLearning.size() > 2 && scriptureListLearning.get(0).getVerse() != null && scriptureListLearning.get(1).getVerse() != null && scriptureListLearning.get(2).getVerse() != null) {
                    Random generate = new Random();
                    final int random = generate.nextInt(100) + 1;
                    caseNumber = calculateCaseNumber(random);
                    switch (caseNumber) {
                        case 1:
                            resultVerse = scriptureListLearning.get(0);
                            break;
                        case 2:
                            resultVerse = scriptureListLearning.get(1);
                            break;
                        case 3:
                            resultVerse = scriptureListLearning.get(2);
                            break;
                        default:
                            resultVerse = scriptureListLearning.get(0);
                    }
                }else if(scriptureListLearning != null && scriptureListLearning.size() == 2 && scriptureListLearning.get(0).getVerse() != null && scriptureListLearning.get(1).getVerse() != null){
                    Random generate = new Random();
                    final int random = generate.nextInt(100) + 1;
                    caseNumber = calculateCaseNumber(random);
                    switch (caseNumber) {
                        case 1:
                            resultVerse = scriptureListLearning.get(0);
                            break;
                        case 2:
                        case 3:
                            resultVerse = scriptureListLearning.get(1);
                            break;
                        default:
                            resultVerse = scriptureListLearning.get(0);
                    }
                }else if(scriptureListLearning != null && scriptureListLearning.size() == 1 && scriptureListLearning.get(0).getVerse() != null){
                    Random generate = new Random();
                    final int random = generate.nextInt(100) + 1;
                    caseNumber = calculateCaseNumber(random);
                    switch (caseNumber) {
                        case 1:
                        case 2:
                        case 3:
                            resultVerse = scriptureListLearning.get(0);
                            break;
                        default:
                            resultVerse = scriptureListLearning.get(0);
                    }
                }
                verseCallback.onResponse(resultVerse);
            }

            @Override
            public void onFailure(Exception e) {

            }
        };
        getLocalQuizVerses(quizVersesCallback, applicationContext);
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

    public int getLocalQuizListSize(final Context context) {
        if(scriptureManager == null) {
            scriptureManager = new ScriptureManager(context);
        }
        return scriptureManager.getScriptureSet(MemoryListContract.LearningSetEntry.TABLE_NAME).size();
    }

    public void rebuildLocalDb(final Context context) {
        final UserPreferences mPrefs = new UserPreferences();
        final UserPreferencesModel mPrefsModel = new UserPreferencesModel();
        if(scriptureManager == null) {
            scriptureManager = new ScriptureManager(context);
        }
        BaseCallback<List<ScriptureData>> upcomingCallback = new BaseCallback<List<ScriptureData>>() {
            @Override
            public void onResponse(List<ScriptureData> response) {
                VerseOperations.getInstance(context).nukeDb();
                if(response.size() > 0){
                    for(ScriptureData verse : response){
                        scriptureManager.addVerse(verse, MemoryListContract.CurrentSetEntry.TABLE_NAME);
                    }
                }
                BaseCallback<List<ScriptureData>> quizCallback = new BaseCallback<List<ScriptureData>>() {
                    @Override
                    public void onResponse(List<ScriptureData> response) {
                        if(response.size() > 0){
                            for(ScriptureData verse : response){
                                scriptureManager.addVerse(verse, MemoryListContract.LearningSetEntry.TABLE_NAME);
                            }
                        }
                        BaseCallback<List<ScriptureData>> forgottenVersesCallback = new BaseCallback<List<ScriptureData>>() {
                            @Override
                            public void onResponse(List<ScriptureData> response) {
                                if(response.size() > 0){
                                    for(ScriptureData verse : response){
                                        scriptureManager.addVerse(verse, MemoryListContract.ForgottenSetEntry.TABLE_NAME);
                                    }
                                }
                                BaseCallback<List<ScriptureData>> memorizedCallback = new BaseCallback<List<ScriptureData>>() {
                                    @Override
                                    public void onResponse(List<ScriptureData> response) {
                                        if(response.size() > 0){
                                            for(ScriptureData verse : response){
                                                scriptureManager.addVerse(verse, MemoryListContract.MemorizedSetEntry.TABLE_NAME);
                                            }
                                        }
                                        mPrefs.setRebuildError(false, context);
                                        mPrefsModel.initAllData(context, mPrefs);
                                        DataStore.getInstance().saveUserPrefs(mPrefsModel, context);
                                        context.startActivity(new Intent(context, MainActivity.class));
                                    }

                                    @Override
                                    public void onFailure(Exception e) {
                                        mPrefs.setRebuildError(true, context);
                                        context.startActivity(new Intent(context, MainActivity.class));
                                    }
                                };
                                getMemorizedVerses(memorizedCallback, context);
                            }

                            @Override
                            public void onFailure(Exception e) {
                                mPrefs.setRebuildError(true, context);
                                context.startActivity(new Intent(context, MainActivity.class));
                            }
                        };
                        getForgottenVerses(forgottenVersesCallback, context);
                    }

                    @Override
                    public void onFailure(Exception e) {
                        mPrefs.setRebuildError(true, context);
                        context.startActivity(new Intent(context, MainActivity.class));
                    }
                };
                getQuizVerses(quizCallback, context);
            }

            @Override
            public void onFailure(Exception e) {
                mPrefs.setRebuildError(true, context);
                context.startActivity(new Intent(context, MainActivity.class));
            }
        };
        getUpcomingVerses(upcomingCallback, context);
    }

    public void saveUserPrefs(UserPreferencesModel mPrefsModel, Context applicationContext) {
        FirebaseDb.getInstance().saveUserPrefs(mPrefsModel, applicationContext);
    }

    public void getUserPrefs(Context context, BaseCallback<UserPreferencesModel> userPrefsCallback){
        FirebaseDb.getInstance().getUserPrefsFromFirebaseDb(context, userPrefsCallback);
    }
}
