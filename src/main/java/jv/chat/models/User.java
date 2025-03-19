package jv.chat.models;

public class User {
    private int id;
    private String username;
    private String password;

    public User(int id, String username, String password/*, boolean status*/) {
        this.id = id;
        this.username = username;
        this.password = password;
    }

    public User(int id, String username) {
        this.id = id;
        this.username = username;
    }

    public int getId() { return id; }
    public String getUsername() { return username; }
    public String getPassword() { return password; }
}