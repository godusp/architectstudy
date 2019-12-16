package com.hh;

import com.hh.cmdhandler.UserEntryCmdHandler;
import com.hh.cmdhandler.WhoElseIsHereCmdHandler;
import com.hh.model.User;
import com.hh.model.UserManager;
import com.hh.msg.GameMsgProtocol;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.AttributeKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

public class GameMsgHandler extends SimpleChannelInboundHandler<Object> {

    private static final Logger LOGGER = LoggerFactory.getLogger(GameMsgHandler.class);

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
        Broadcaster.addChannel(ctx.channel());
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        super.handlerRemoved(ctx);
        Integer userId = (Integer) ctx.channel().attr(AttributeKey.valueOf("userId")).get();

        if(userId==null){
            return;
        }
        UserManager.removerUserById(userId);

        GameMsgProtocol.UserQuitResult.Builder resultBuilder = GameMsgProtocol.UserQuitResult.newBuilder();
        resultBuilder.setQuitUserId(userId);
        Broadcaster.broadcast(resultBuilder.build());

    }

    protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {

        if(msg instanceof GameMsgProtocol.UserEntryCmd){
            new UserEntryCmdHandler().handle(ctx, msg);
        } else if(msg instanceof GameMsgProtocol.WhoElseIsHereCmd){
            new WhoElseIsHereCmdHandler().handle(ctx, msg);
        }

    }
}
