package com.hh.cmdhandler;

import com.google.protobuf.GeneratedMessageV3;
import com.hh.msg.GameMsgProtocol;

import java.util.HashMap;
import java.util.Map;

public class CmdHandlerFactory {
    private static final Map<Class<?> ,CmdHandler<? extends GeneratedMessageV3>> CMD_HANDLER_MAP = new HashMap<>();

    static {
        CMD_HANDLER_MAP.put(GameMsgProtocol.UserEntryCmd.class,new UserEntryCmdHandler());
        CMD_HANDLER_MAP.put(GameMsgProtocol.WhoElseIsHereCmd.class,new WhoElseIsHereCmdHandler());
    }


    public static CmdHandler<? extends GeneratedMessageV3> getCmdHanlder(Class<?> clazz){
        if(clazz==null){
            return null;
        }
        return CMD_HANDLER_MAP.get(clazz);
    }


}
