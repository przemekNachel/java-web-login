import java.time.LocalDateTime;
import java.util.ArrayList;

public class SessionDao {

    private ArrayList<Session> sessions = new ArrayList<>();

    public void addSession(Session session) {
        sessions.add(session);
    }

    public Session getSessionById(String cookie) {
//
        sessions.add(new Session("d0c13d37-ec85-425d-8991-3efb4d1d63f8", "tadek",
                LocalDateTime.now(), LocalDateTime.now().plusMinutes(30), LocalDateTime.now()));
//
        for (Session session : sessions) {
            if (cookie.equals(session.getSessionId())) {
                return session;
            }
        }
        return null;
    }
}
