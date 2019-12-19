package com.hh;

import com.google.protobuf.GeneratedMessageV3;
import com.google.protobuf.Message;
import com.hh.msg.GameMsgProtocol;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

public class GameMsgRecognizer {

    private static final Map<Integer,GeneratedMessageV3> DECODER_TYPE_MAP = new HashMap<>();

    private static final Map<Class<?>,Integer> ENCODER_CODE_MAP = new HashMap<>();

    private static final Logger LOGGER = LoggerFactory.getLogger(GameMsgRecognizer.class);

    public static void init() {
        Class<GameMsgProtocol> gameMsgProtocolClass = GameMsgProtocol.class;
        Class<?>[] msgClasses = gameMsgProtocolClass.getDeclaredClasses();

        GameMsgProtocol.MsgCode[] codes = GameMsgProtocol.MsgCode.values();
        for (GameMsgProtocol.MsgCode code : codes) {
            String name = code.name();
            name = name.toLowerCase();
            String[] subNames = name.split("_");
            StringBuilder sb = new StringBuilder();
            for (String subName : subNames) {
                sb.append(subName.substring(0,1).toUpperCase());
                sb.append(subName.substring(1));
            }

            String msgClassName = sb.toString();
            for (Class<?> msgClass : msgClasses) {
                if(!GeneratedMessageV3.class.isAssignableFrom(msgClass)){
                    continue;
                }
                if(msgClass.getSimpleName().equals(msgClassName)){
                    try {
                        GeneratedMessageV3 instance = (GeneratedMessageV3) msgClass.getDeclaredMethod("getDefaultInstance").invoke(msgClass);
                        DECODER_TYPE_MAP.put(code.getNumber(),instance);
                        LOGGER.info("{}<====>{}",code.getNumber(),msgClass);
                        ENCODER_CODE_MAP.put(msgClass,code.getNumber());
                        LOGGER.info("{}<====>{}",msgClass,code.getNumber());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

            }
        }



    }

    private GameMsgRecognizer(){}

    public static Message.Builder getBuilderByMsgCode(int msgCode){
        if(msgCode < 0){
            return null;
        }
        GeneratedMessageV3 msg = DECODER_TYPE_MAP.get(msgCode);
        return msg.newBuilderForType();
    }

    public static int getMsgCodeByMsgType(Class<?> claszz){
        if(claszz==null){
            return -1;
        }
        Integer code = ENCODER_CODE_MAP.get(claszz);
        return code != null ? code.intValue() : -1;
    }


}
