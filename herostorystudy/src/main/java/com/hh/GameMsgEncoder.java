package com.hh;

import com.google.protobuf.GeneratedMessageV3;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import io.netty.channel.ChannelPromise;
import io.netty.handler.codec.http.websocketx.BinaryWebSocketFrame;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GameMsgEncoder extends ChannelOutboundHandlerAdapter {
    private static final Logger LOGGER = LoggerFactory.getLogger(GameMsgEncoder.class);


    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
        if(!(msg instanceof GeneratedMessageV3)){
            super.write(ctx,msg,promise);
            return;
        }

        int msgCode = GameMsgRecognizer.getMsgCodeByMsgType(msg.getClass());

        byte[] msgBody = ((GeneratedMessageV3) msg).toByteArray();

        ByteBuf buffer = ctx.alloc().buffer();
        buffer.writeShort(0);
        buffer.writeShort(msgCode);
        buffer.writeBytes(msgBody);

        BinaryWebSocketFrame frame = new BinaryWebSocketFrame(buffer);
        super.write(ctx,frame,promise);
    }
}
