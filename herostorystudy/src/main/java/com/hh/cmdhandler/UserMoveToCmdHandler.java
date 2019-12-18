package com.hh.cmdhandler;

import com.hh.Broadcaster;
import com.hh.model.MoveState;
import com.hh.model.User;
import com.hh.model.UserManager;
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

        User user = UserManager.getUserById(userId);
        MoveState moveState = user.moveState;
        moveState.fromPosX = cmd.getMoveFromPosX();
        moveState.fromPosY = cmd.getMoveFromPosY();
        moveState.toPosX = cmd.getMoveToPosX();
        moveState.toPosY = cmd.getMoveToPosY();
        moveState.stateTime = System.currentTimeMillis();

        GameMsgProtocol.UserMoveToResult.Builder resuletBuilder = GameMsgProtocol.UserMoveToResult.newBuilder();
        resuletBuilder.setMoveUserId(userId);
        resuletBuilder.setMoveFromPosX(moveState.fromPosX);
        resuletBuilder.setMoveFromPosY(moveState.fromPosY);
        resuletBuilder.setMoveToPosX(moveState.toPosX);
        resuletBuilder.setMoveToPosY(moveState.toPosY);
        resuletBuilder.setMoveStartTime(moveState.stateTime);
        GameMsgProtocol.UserMoveToResult result = resuletBuilder.build();
        Broadcaster.broadcast(result);
    }
}
