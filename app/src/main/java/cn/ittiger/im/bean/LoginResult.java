package cn.ittiger.im.bean;

/**
 * Created by ylhu on 16-12-14.
 */
public class LoginResult {

    private User mUser;
    private boolean mSuccess;
    private String mErrorMsg;

    public LoginResult(User user, boolean success) {

        mUser = user;
        mSuccess = success;
    }

    public LoginResult(boolean success, String errorMsg) {

        mSuccess = success;
        mErrorMsg = errorMsg;
    }

    public boolean isSuccess() {

        return mSuccess;
    }

    public String getErrorMsg() {

        return mErrorMsg;
    }

    public User getUser() {

        return mUser;
    }
}
