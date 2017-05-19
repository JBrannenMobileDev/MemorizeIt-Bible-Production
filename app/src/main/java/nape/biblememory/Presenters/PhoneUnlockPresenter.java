package nape.biblememory.Presenters;


public interface PhoneUnlockPresenter {
    void onCloseClicked();
    void onCheckAnswerClicked();
    void onYesClicked();
    void onNoClicked();
    void onRequestData();
    void onMoreSwitchStateChanged(boolean isChecked);
    void onHintClicked();
}
