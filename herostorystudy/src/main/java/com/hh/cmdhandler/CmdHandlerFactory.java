package com.hh.cmdhandler;

import com.google.protobuf.GeneratedMessageV3;
import com.hh.msg.GameMsgProtocol;
import com.hh.util.PackageUtil;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class CmdHandlerFactory {
    private static final Map<Class<?> ,CmdHandler<? extends GeneratedMessageV3>> CMD_HANDLER_MAP = new HashMap<>();

    static {
//        CMD_HANDLER_MAP.put(GameMsgProtocol.UserEntryCmd.class,new UserEntryCmdHandler());
//        CMD_HANDLER_MAP.put(GameMsgProtocol.WhoElseIsHereCmd.class,new WhoElseIsHereCmdHandler());
        String pakageName = CmdHandlerFactory.class.getPackage().getName();
        Set<Class<?>> listSubClazz = PackageUtil.listSubClazz(pakageName, true, CmdHandler.class);
        for (Class<?> subClazz : listSubClazz) {
            if((subClazz.getModifiers() & Modifier.ABSTRACT) != 0){
                continue;
            }

            Method[] methods = subClazz.getDeclaredMethods();

            for (Method method : methods) {
                if(!(method.getName().equals("handle"))){
                    continue;
                }
                Class<?>[] parameterTypes = method.getParameterTypes();
                if(parameterTypes.length != 2){
                    continue;
                }
                try {
                    CmdHandler<?> handler = (CmdHandler) subClazz.newInstance();
                    CMD_HANDLER_MAP.put(parameterTypes[1],handler);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }


    public static CmdHandler<? extends GeneratedMessageV3> getCmdHanlder(Class<?> clazz){
        if(clazz==null){
            return null;
        }
        return CMD_HANDLER_MAP.get(clazz);
    }


}
