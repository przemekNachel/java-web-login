import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public abstract class SqliteDao {

    private DatabaseConnection connection = DatabaseConnection.getInstance();

    ArrayList<ArrayList<String>> sendQuery(String query, String[] placeholderStrings, String[] columnsToReturn) throws SQLException{
        ArrayList<ArrayList<String>> result = new ArrayList<>();
        PreparedStatement preparedStatement = connection.getConnection().prepareStatement(query);
        int i = 1;
        for (String string : placeholderStrings) {
            if (string.matches("\\d+")) {
                preparedStatement.setInt(i++, Integer.valueOf(string));
            } else {
                preparedStatement.setString(i++, string);
            }

        }
        for (ResultSet rs = preparedStatement.executeQuery(); rs.next();) {
            ArrayList<String> row = new ArrayList<>();
            for (String columnLabel : columnsToReturn) {
                row.add(rs.getString(columnLabel));
            }
            result.add(row);
        }
        return result;
    }

    void sendQuery(String query, String[] placeholderStrings) throws SQLException{
        PreparedStatement preparedStatement = connection.getConnection().prepareStatement(query);
        int i = 1;
        for (String string : placeholderStrings) {
            if (string.matches("\\d+")) {
                preparedStatement.setInt(i++, Integer.valueOf(string));
            } else {
                preparedStatement.setString(i++, string);
            }
        }
        preparedStatement.executeUpdate();
    }
}
