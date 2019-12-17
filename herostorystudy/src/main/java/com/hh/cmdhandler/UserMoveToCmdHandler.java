package com.hh.cmdhandler;

import com.hh.Broadcaster;
import com.hh.msg.GameMsgProtocol;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.AttributeKey;

public class UserMoveToCmdHandler implements CmdHandler<GameMsgProtocol.UserMoveToCmd> {

    @Override
    public void handle(ChannelHandlerContext ctx, GameMsgProtocol.UserMoveToCmd cmd) {
        Integer userId = (Integer) ctx.channel().attr(AttributeKey.valueOf("userId")).get();
        if(userId == null){
            return;
        }
        GameMsgProtocol.UserMoveToResult.Builder resuletBuilder = GameMsgProtocol.UserMoveToResult.newBuilder();
        resuletBuilder.setMoveUserId(userId);
        resuletBuilder.setMoveToPosX(cmd.getMoveToPosX());
        resuletBuilder.setMoveToPosY(cmd.getMoveToPosY());
        GameMsgProtocol.UserMoveToResult result = resuletBuilder.build();
        Broadcaster.broadcast(result);
    }
}
