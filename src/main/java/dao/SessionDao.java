package dao;

import model.Session;
import model.User;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Map;

public class SessionDao extends SqliteDao {

    public void addSession(Session session) throws SQLException {
        String query = "INSERT INTO Sessions VALUES (?, ?, ?, ?, ?)";
        String[] placeholderStrings = {
                session.getSessionId(),
                session.getUserName(),
                session.getCreateDate().toString(),
                session.getExpireDate().toString(),
                session.getLastAccessDate().toString()};
        sendQuery(query, placeholderStrings);
    }

    public void deleteSession(Session session) throws SQLException {
        String query = "DELETE FROM Sessions WHERE SessionId = ?";
        String[] placeholderStrings = {session.getSessionId()};
        sendQuery(query, placeholderStrings);
    }

    public void updateLastAccessDate(Session session) throws SQLException {
        String query = "UPDATE Sessions SET LastAccessDate = ? WHERE SessionId = ?";
        String[] placeholderStrings = {LocalDateTime.now().toString(), session.getSessionId()};
        sendQuery(query, placeholderStrings);
    }

    public Session getSessionById(String cookie) throws SQLException {
        String query = "SELECT * FROM Sessions WHERE SessionId = ?";
        String[] placeholderStrings = {cookie};
        String[] columns = {"SessionId", "UserName", "CreateDate", "ExpireDate", "LastAccessDate"};
        ArrayList<ArrayList<String>> sessionsData = sendQuery(query, placeholderStrings, columns);
        Session session = null;
        if (sessionsData.size() > 0) {
            ArrayList<String> sessionData = sessionsData.get(0);
            String sessionId = sessionData.get(0);
            String userName = sessionData.get(1);
            LocalDateTime createDate = LocalDateTime.parse(sessionData.get(2));
            LocalDateTime expireDate = LocalDateTime.parse(sessionData.get(3));
            LocalDateTime lastAccessDate = LocalDateTime.parse(sessionData.get(4));
            session = new Session(sessionId, userName, createDate, expireDate, lastAccessDate);
        }
        return session;
    }
}
