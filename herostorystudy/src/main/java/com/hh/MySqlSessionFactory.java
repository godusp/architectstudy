package com.hh;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.IOException;

public final class MySqlSessionFactory {

    private static SqlSessionFactory sqlSessionFactory;

    private MySqlSessionFactory(){}

    static {
        try {
            sqlSessionFactory = new SqlSessionFactoryBuilder().build(Resources.getResourceAsStream("MyBatisConfig.xml"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static SqlSession openSession(){
        if(sqlSessionFactory==null){
            throw new RuntimeException("sqlSessionFactory未初始化");
        }
        return sqlSessionFactory.openSession(true);
    }
}
