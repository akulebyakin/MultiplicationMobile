package pro.kulebyakin.multiplicationmobile;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class User implements Comparable<User>{

    public String username;
    public int result;

    public User() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public User(String username, int result) {
        this.username = username;
        this.result = result;
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
