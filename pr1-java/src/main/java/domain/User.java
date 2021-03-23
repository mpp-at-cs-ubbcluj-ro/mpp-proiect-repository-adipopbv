package domain;

public class User extends Entity<Integer> {
    private String username;
    private String status;

    public User(String username) {
        this.username = username;
        this.status = "logged-out";
    }

    public User(String username, String status) {
        this.username = username;
        this.status = status;
    }

    public User(Integer id, String username, String status) {
        super(id);
        this.username = username;
        this.status = status;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "User{" +
                "id='" + getId() + '\'' +
                ", username='" + username + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}
