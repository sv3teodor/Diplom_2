package pojo;

import utils.BasePojo;

public class User extends BasePojo {
    private String email;
    private String password;
    private String name;

    public User() {

    }

    public User(String email, String password, String name) {
        this.email = email;
        this.name = name;
        this.password = password;
    }

    @Override
    public String toString() {
        return String.format("Email=%s, Name=%s, Password=%s", this.email, this.name, this.password);
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}

