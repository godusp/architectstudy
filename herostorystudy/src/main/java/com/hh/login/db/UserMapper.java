package com.hh.login.db;

public interface UserMapper {
    UserEntity queryUserByUserName(String userName);
    int insertUser(UserEntity user);
}
