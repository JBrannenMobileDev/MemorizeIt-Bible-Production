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
import nape.biblememory.Models.UserPreferencesModel;
import nape.biblememory.UserPreferences;
import nape.biblememory.data_store.FirebaseDb.FirebaseDb;
import nape.biblememory.data_store.FirebaseDb.User;
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

    public void saveUser(User user, Context applicationContext){
        FirebaseDb.getInstance().saveUser(user, applicationContext);
    }

    public void getUsers(Context application, BaseCallback<List<User>> usersCallback){
        //TODO finish someday when needed
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
        VerseOperations.getInstance(applicationContext).addVerse(verse, MemoryListContract.RememberedSetEntry.TABLE_NAME);
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
        VerseOperations.getInstance(context).removeVerse(verse.getVerseLocation(), MemoryListContract.RememberedSetEntry.TABLE_NAME);
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
            memorizedCallback.onResponse(scriptureManager.getScriptureSet(MemoryListContract.RememberedSetEntry.TABLE_NAME));
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

    public void getLocalMemorizedVerses(BaseCallback<List<ScriptureData>> memorizedCallback, Context applicationContext){
        if(scriptureManager == null) {
            scriptureManager = new ScriptureManager(applicationContext);
        }
        memorizedCallback.onResponse(scriptureManager.getScriptureSet(MemoryListContract.RememberedSetEntry.TABLE_NAME));
    }

    public void updateQuizVerse(ScriptureData verse, Context applcationContext){
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
                        BaseCallback<List<ScriptureData>> memorizedCallback = new BaseCallback<List<ScriptureData>>() {
                            @Override
                            public void onResponse(List<ScriptureData> response) {
                                if(response.size() > 0){
                                    for(ScriptureData verse : response){
                                        scriptureManager.addVerse(verse, MemoryListContract.RememberedSetEntry.TABLE_NAME);
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
