package pack1;

public class User {
    private int id;
    private String username;
    private String fullName;
    private String password;
    private int score;

    public User(int id, String username, String fullName, String password, int score) {
        this.id = id;
        this.username = username;
        this.fullName = fullName;
        this.password = password;
        this.score = score;
    }

    public int getId() { return id; }
    public String getUsername() { return username; }
    public String getFullName() { return fullName; }
    public String getPassword() { return password; }
    public int getScore() { return score; }
    public void setScore(int score) { this.score = score; }
}