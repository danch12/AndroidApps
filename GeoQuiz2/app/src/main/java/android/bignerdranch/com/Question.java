package android.bignerdranch.com;

public class Question {
    private int mTextResId;
    private boolean mAnswerTrue;

    public Question(int textResId, boolean answerTrue){
        mTextResId=textResId;
        mAnswerTrue=answerTrue;
    }

    public boolean ismAnswerTrue() {
        return mAnswerTrue;
    }

    public void setAnswerTrue(boolean AnswerTrue) {
        this.mAnswerTrue = mAnswerTrue;
    }

    public int getTextResId() {
        return mTextResId;
    }

    public void setTextResId(int TextResId) {
        this.mTextResId = mTextResId;
    }
}
