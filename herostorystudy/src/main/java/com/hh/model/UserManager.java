package com.hh.model;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class UserManager {
    private static final Map<Integer, User> USER_MAP = new HashMap<Integer, User>();

    private UserManager(){}

    public static void addUser(User user){
        if(user==null){
            return;
        }
        USER_MAP.put(user.userId,user);
    }


    public static void removerUserById(int userId){
        USER_MAP.remove(userId);
    }

    public static Collection<User> listUser(){
        return USER_MAP.values();
    }

}