package it.unito.di.justread.model;

/**
 * @date 18-ott-2017
 * @author Arnaudo Enrico, Giraudo Paolo, Impeduglia Alessia.
 */
public class User {

    private String email;
    private String token;
    private String name;
    private String surname;
    private int level;
    private String profileImage;
    private String address;

    public User() {
    }

    public String getEmail() {
        return email;
    }

    public String getToken() {
        return token;
    }

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }

    public int getLevel() {
        return level;
    }

    public String getProfileImage() {
        return profileImage;
    }

    public String getAddress() {
        return address;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Override
    public String toString() {
        return "User{" + "email=" + email + '}';
    }
}
