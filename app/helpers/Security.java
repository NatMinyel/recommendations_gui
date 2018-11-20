package app.helpers;

import app.models.User;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Security {
    public static void loginUser(ResultSet resultSet) throws SQLException {
        User.username = resultSet.getString("username");
        User.email = resultSet.getString("email");
    }


}
