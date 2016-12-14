package cn.ittiger.im.bean;

/**
 * Created by ylhu on 16-12-14.
 */
public class LoginResult {

    private boolean mSuccess;
    private String mErrorMsg;

    public LoginResult(boolean success) {

        mSuccess = success;
    }

    public LoginResult(boolean success, String errorMsg) {

        mSuccess = success;
        mErrorMsg = errorMsg;
    }

    public boolean isSuccess() {

        return mSuccess;
    }

    public void setSuccess(boolean success) {

        mSuccess = success;
    }

    public String getErrorMsg() {

        return mErrorMsg;
    }

    public void setErrorMsg(String errorMsg) {

        mErrorMsg = errorMsg;
    }
}
