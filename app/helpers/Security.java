package app.helpers;

import app.models.User;
import org.json.JSONObject;

import java.io.File;


public class Security {
    public static void loginUser(JSONObject user) {
        try {
            User.full_name = user.getString("name");
        } catch (RuntimeException e) {
            User.full_name = "";
        }
        User.email = user.getString("email");
        User.token = user.getString("token");
        try {
            User.profile_picture = user.getString("profilePicture");
            File proPic = new File(User.profile_picture);
            if (!proPic.exists()) throw new RuntimeException();
        } catch (RuntimeException re) {
            User.profile_picture = "/home/nathnael/Dev/recommendation/src/app/assets/images/user.png";
        }
    }

    public static void loginUser(String token) {
        JSONObject user = Requests.getRequestObj("http://localhost:8080/user/show/", token);
        user.put("token", token);
        loginUser(user);
    }


}
