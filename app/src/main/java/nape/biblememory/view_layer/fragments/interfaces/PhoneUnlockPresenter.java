package nape.biblememory.view_layer.fragments.interfaces;


public interface PhoneUnlockPresenter {
    void onCloseClicked();
    void onCheckAnswerClicked();
    void onYesClicked();
    void onNoClicked();
    void onRequestData();
    void onMoreSwitchStateChanged(boolean isChecked);
    void onHintClicked();
    void onRequestReviewData();
}
