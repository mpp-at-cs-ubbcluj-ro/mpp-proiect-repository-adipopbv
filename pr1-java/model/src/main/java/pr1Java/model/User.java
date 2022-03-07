package pr1Java.model;

public class User extends Entity<String> {
    private String password;

    public User() {

    }

    public User(String username, String password) {
        super(username);
        this.password = password;
    }

    public String getUsername() {
        return getId();
    }

    public void setUsername(String username) {
        setId(username);
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "User{" +
                "username='" + getUsername() + '\'' +
                '}';
    }
}
