import java.sql.SQLException;
import java.util.ArrayList;

public class UserDao extends SqliteDao {

    public void addUser(String username, String password) throws SQLException {
        String query = "INSERT INTO Users VALUES (?, ?)";
        String[] placeholderStrings = {username, password};
        sendQuery(query, placeholderStrings);
    }

    public String getUserByUsername(String username, String password) throws SQLException {
        String query = "SELECT * FROM Users WHERE UserName = ? AND Password = ?";
        String[] placeholderStrings = {username, password};
        String[] columns = {"Username"};
        ArrayList<ArrayList<String>> usersData = sendQuery(query, placeholderStrings, columns);
        Session session = null;
        if (usersData.size() > 0) {
            ArrayList<String> userData = usersData.get(0);
            return userData.get(0);
        }
        return null;
    }
}
