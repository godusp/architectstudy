package com.hh;

import com.google.protobuf.GeneratedMessageV3;
import com.hh.msg.GameMsgProtocol;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.websocketx.BinaryWebSocketFrame;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

public class GameMsgDecoder extends ChannelInboundHandlerAdapter {

    private static final Logger LOGGER = LoggerFactory.getLogger(GameMsgDecoder.class);

    private static final Map<Integer,GeneratedMessageV3> MSG_MAP = new HashMap<>();

    static {
        Class<GeneratedMessageV3> generatedMessageV3Class = GeneratedMessageV3.class;
        Class<?>[] msgClasses = generatedMessageV3Class.getDeclaredClasses();

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
                if(msgClass.getName().equals(msgClassName)){
                    try {
                        GeneratedMessageV3 o = (GeneratedMessageV3) msgClass.newInstance();
                        MSG_MAP.put(code.getNumber(),o);
                    } catch (InstantiationException e) {
                        e.printStackTrace();
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        LOGGER.info("收到的消息:{}",msg);

        if(! (msg instanceof BinaryWebSocketFrame)){
            return;
        }

        BinaryWebSocketFrame frame = (BinaryWebSocketFrame) msg;
        ByteBuf byteBuf = frame.content();

        //消息长度
        byteBuf.readShort();
        //消息编号
        short msgCode = byteBuf.readShort();

        byte[] msgBody = new byte[byteBuf.readableBytes()];
        byteBuf.readBytes(msgBody);

        GeneratedMessageV3 cmd = MSG_MAP.get(msgCode);

//        switch (msgCode) {
//            case GameMsgProtocol.MsgCode.USER_ENTRY_CMD_VALUE:
//                cmd = GameMsgProtocol.UserEntryCmd.parseFrom(msgBody);
//                break;
//            case GameMsgProtocol.MsgCode.WHO_ELSE_IS_HERE_CMD_VALUE:
//                cmd = GameMsgProtocol.WhoElseIsHereCmd.parseFrom(msgBody);
//                break;
//        }

        if(cmd != null){
            ctx.fireChannelRead(cmd);
        }

    }
}
