package domain;

public class User extends Entity<Integer>{
    private String username;
    private UserStatus status = UserStatus.LOGGEDOUT;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public UserStatus getStatus() {
        return status;
    }

    public void setStatus(UserStatus status) {
        this.status = status;
    }

    public User(String username, UserStatus status) {
        this.username = username;
        this.status = status;
    }
}
