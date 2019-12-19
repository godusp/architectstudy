package com.hh.login;

import com.hh.MySqlSessionFactory;
import com.hh.login.db.UserEntity;
import com.hh.login.db.UserMapper;
import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LoginService {
    private static Logger LOGGER = LoggerFactory.getLogger(LoginService.class);

    private LoginService(){}

    private static LoginService INSTANCE = new LoginService();

    public static LoginService getInstance() {
        return INSTANCE;
    }

    public UserEntity login(String userName,String password){
        if(userName==null || password==null){
            return null;
        }
        try (SqlSession session = MySqlSessionFactory.openSession()){
            UserMapper mapper = session.getMapper(UserMapper.class);
            UserEntity userEntity = mapper.queryUserByUserName(userName);
            if(userEntity==null){
                userEntity = new UserEntity(userName, password, "1");
                mapper.insertUser(userEntity);
            } else {
                if(!password.equals(userEntity.getPassword())){
                    throw new RuntimeException("密码错误");
                }
            }
            return userEntity;
        } catch (Exception e){
            LOGGER.error(e.getMessage(),e);
            throw new RuntimeException(e);
        }
    }
}
