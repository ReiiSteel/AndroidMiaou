package martinflambard.androidmiaou2;

/**
 * Created by William on 09/02/2017.
 */

public class Message {
    private String message;
    private String timestamp;
    private int type;
    private String user;

    public Message(String message, String timestamp, int type, String user){
        this.message = message;
        this.timestamp = timestamp;
        this.type = type;
        this.user = user;
    }

    public String getMessage() {
        return message;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public int getType() {
        return type;
    }

    public String getUser() {
        return user;
    }
}
