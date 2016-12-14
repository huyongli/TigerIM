package cn.ittiger.im.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by laohu on 16-12-14.
 */
public class User implements Parcelable {

    private String mUsername;
    private String mPassword;

    public User() {

    }

    public User(String username, String password) {

        mUsername = username;
        mPassword = password;
    }

    public String getUsername() {

        return mUsername;
    }

    public void setUsername(String username) {

        mUsername = username;
    }

    public String getPassword() {

        return mPassword;
    }

    public void setPassword(String password) {

        mPassword = password;
    }


    @Override
    public int describeContents() {

        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeString(this.mUsername);
        dest.writeString(this.mPassword);
    }

    protected User(Parcel in) {

        this.mUsername = in.readString();
        this.mPassword = in.readString();
    }

    public static final Parcelable.Creator<User> CREATOR = new Parcelable.Creator<User>() {
        @Override
        public User createFromParcel(Parcel source) {

            return new User(source);
        }

        @Override
        public User[] newArray(int size) {

            return new User[size];
        }
    };
}
