package com.hh.login;

import com.hh.MySqlSessionFactory;
import com.hh.async.AsyncOperation;
import com.hh.async.AsyncOprationProcessor;
import com.hh.login.db.UserEntity;
import com.hh.login.db.UserMapper;
import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.function.Consumer;

public class LoginService {
    private static Logger LOGGER = LoggerFactory.getLogger(LoginService.class);

    private LoginService(){}

    private static LoginService INSTANCE = new LoginService();

    public static LoginService getInstance() {
        return INSTANCE;
    }

    public void login(String userName, String password, Consumer<UserEntity> callback){
        if(userName==null || password==null){
            return;
        }

        LoginAsynsOperation loginAsynsOperation = new LoginAsynsOperation(userName, password) {
            @Override
            public void doFinish() {
                if(callback != null){
                    callback.accept(this.getUserEntity());
                }
            }
        };
        AsyncOprationProcessor.getInstance().process(loginAsynsOperation);
    }


    private class LoginAsynsOperation implements AsyncOperation {
        private final String userName;
        private final String password;
        private UserEntity userEntity;

        public UserEntity getUserEntity() {
            return userEntity;
        }

        public LoginAsynsOperation(String userName, String password) {
            this.userName = userName;
            this.password = password;
        }

        @Override
        public int bindId() {
            return userName.charAt(userName.length() - 1);
        }

        @Override
        public void doAsync() {
            try {
                LOGGER.info("当前线程：{}", Thread.currentThread().getName());
                try (SqlSession session = MySqlSessionFactory.openSession()){
                    UserMapper mapper = session.getMapper(UserMapper.class);
                    UserEntity userEntity = mapper.queryUserByUserName(userName);
                    if(userEntity==null){
                        userEntity = new UserEntity(userName, password, "Hero_Shaman");
                        mapper.insertUser(userEntity);
                    } else {
                        if(!password.equals(userEntity.getPassword())){
                            throw new RuntimeException("密码错误");
                        }
                    }
                    this.userEntity = userEntity;
                } catch (Exception e){
                    LOGGER.error(e.getMessage(),e);
                    throw new RuntimeException(e);
                }
            } catch (Exception e) {
                LOGGER.error(e.getMessage(), e);
            }
        }
    }
}
