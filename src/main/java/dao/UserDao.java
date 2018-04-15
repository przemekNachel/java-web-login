package dao;

import model.Session;
import model.User;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Map;

public class UserDao extends SqliteDao {

    public void addUser(String username, String password) throws SQLException {
        String query = "INSERT INTO Users VALUES (?, ?)";
        String[] placeholderStrings = {username, password};
        sendQuery(query, placeholderStrings);
    }

    public User getUserByFormData(Map<String, String> formData) throws SQLException {
        String query = "SELECT * FROM Users WHERE UserName = ? AND Password = ?";
        String[] placeholderStrings = {formData.get("username"), formData.get("password")};
        String[] columns = {"Username"};
        ArrayList<ArrayList<String>> usersData = sendQuery(query, placeholderStrings, columns);
        if (usersData.size() > 0) {
            return new User(usersData.get(0).get(0));
        }
        return null;
    }
}
