import java.time.LocalDateTime;

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

    public Session(String sessionId, String userName, LocalDateTime expireDate) {
        this.sessionId = sessionId;
        this.userName = userName;
        this.createDate = LocalDateTime.now();
        this.expireDate = expireDate;
        this.lastAccessDate = createDate;
    }
}
