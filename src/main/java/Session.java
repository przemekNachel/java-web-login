import java.time.LocalDateTime;
import java.util.UUID;

public class Session {

    private String sessionId;
    private String userName;
    private LocalDateTime createDate;
    private LocalDateTime expireDate;
    private LocalDateTime lastAccessDate;

    public Session(String sessionId, String userName, LocalDateTime createDate, LocalDateTime expireDate, LocalDateTime lastAccessDate) {
        this.sessionId = sessionId;
        this.userName = userName;
        this.createDate = createDate;
        this.expireDate = expireDate;
        this.lastAccessDate = lastAccessDate;
    }

    public Session(String userName) {
        this.sessionId = generateSessionId();
        this.userName = userName;
        this.createDate = LocalDateTime.now();
        this.expireDate = createDate.plusMinutes(3);
        this.lastAccessDate = createDate;
    }

    private String generateSessionId() {
        return UUID.randomUUID().toString();
    }

    public String getSessionId() {
        return sessionId;
    }

    public String getUserName() {
        return userName;
    }

    public LocalDateTime getCreateDate() {
        return createDate;
    }

    public LocalDateTime getExpireDate() {
        return expireDate;
    }

    public LocalDateTime getLastAccessDate() {
        return lastAccessDate;
    }
}
