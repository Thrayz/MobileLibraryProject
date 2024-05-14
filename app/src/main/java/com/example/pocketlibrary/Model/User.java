package com.example.pocketlibrary.Model;

public class User {
    protected String nickname;
    protected String email;
    protected String code;
    protected String password;

    public User() {}

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCode() {
        return code;
    }

    public void setCode() {
        this.code = hashing();
    }

    public String hashing(){
        String newCode = String.valueOf(this.nickname.length());
        return newCode;
    }

    public void setId(String userId) {
        // Set the Firestore document ID

    }
}
