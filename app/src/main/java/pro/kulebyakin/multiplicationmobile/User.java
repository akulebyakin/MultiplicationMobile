package pro.kulebyakin.multiplicationmobile;

import android.net.Uri;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class User implements Comparable<User>{

    public String username;
    public int result;
    public String userImageURL;

    public User() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public User(String username, int result, String userImageURL) {
        this.username = username;
        this.result = result;
        this.userImageURL = userImageURL;
    }

    @Override
    public int compareTo(User o) {
        if (result > o.result) {
            return -1;
        } else if (result < o.result) {
            return 1;
        } else {
            return 0;
        }
    }
}
