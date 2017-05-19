package system.entities;

/**
 * Created by Dan on 4/26/2017.
 */
public class User {

    // Fields
    private String userName;
    private String host;
    private boolean admin;

    // Constructor
    public User(String userName, String host) {
        this.userName = userName;
        this.host = host;
        this.admin = false;
    }

    // Getters
    public String getUserName() {
        return userName;
    }

    public String getHost() {
        return host;
    }

    public boolean isAdmin() {
        return admin;
    }

    public static User createAdminUser() {
        User adminUser = new User("adminuser", "localhost");
        adminUser.admin = true;

        return adminUser;
    }

    @Override
    public String toString() {
        return "User{" +
                "userName='" + userName + '\'' +
                ", host='" + host + '\'' +
                ", admin=" + admin +
                '}';
    }
}
