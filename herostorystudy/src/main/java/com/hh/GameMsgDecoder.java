package com.hh;

import com.google.protobuf.Message;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.websocketx.BinaryWebSocketFrame;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class GameMsgDecoder extends ChannelInboundHandlerAdapter {

    private static final Logger LOGGER = LoggerFactory.getLogger(GameMsgDecoder.class);


    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        LOGGER.info("收到的消息:{}",msg);

        if(! (msg instanceof BinaryWebSocketFrame)){
            return;
        }

        BinaryWebSocketFrame frame = (BinaryWebSocketFrame) msg;
        ByteBuf byteBuf = frame.content();

        //消息长度
        short i = byteBuf.readShort();
        //消息编号
        int msgCode = byteBuf.readShort();

        Message.Builder builder = GameMsgRecognizer.getBuilderByMsgCode(msgCode);
        if(builder==null){
            LOGGER.error("无法识别的消息，msgCode = {}",msgCode);
            return;
        }

        byte[] msgBody = new byte[byteBuf.readableBytes()];
        byteBuf.readBytes(msgBody);

//        GeneratedMessageV3 cmd = MSG_MAP.get(msgCode);
//        GeneratedMessageV3 cmd = null;

//        switch (msgCode) {
//            case GameMsgProtocol.MsgCode.USER_ENTRY_CMD_VALUE:
//                cmd = GameMsgProtocol.UserEntryCmd.parseFrom(msgBody);
//                break;
//            case GameMsgProtocol.MsgCode.WHO_ELSE_IS_HERE_CMD_VALUE:
//                cmd = GameMsgProtocol.WhoElseIsHereCmd.parseFrom(msgBody);
//                break;
//        }

        builder.clear();
        builder.mergeFrom(msgBody);
        Message message = builder.build();

        if(message != null){
            ctx.fireChannelRead(message);
        }

    }
}
