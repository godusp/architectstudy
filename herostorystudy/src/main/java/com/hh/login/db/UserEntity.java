package com.hh.login.db;

public class UserEntity {
    private int userId;
    private String userName;
    private String password;
    private int heroAvatar;

    public UserEntity( String userName, String password, int heroAvatar) {
        this.userName = userName;
        this.password = password;
        this.heroAvatar = heroAvatar;
    }

    public UserEntity() {
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getHeroAvatar() {
        return heroAvatar;
    }

    public void setHeroAvatar(int heroAvatar) {
        this.heroAvatar = heroAvatar;
    }

    @Override
    public String toString() {
        return "UserEntity{" +
                "userId=" + userId +
                ", userName='" + userName + '\'' +
                ", password='" + password + '\'' +
                ", heroAvatar='" + heroAvatar + '\'' +
                '}';
    }
}
