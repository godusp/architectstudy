package com.hh.cmdhandler;

import com.hh.Broadcaster;
import com.hh.MainThreadProcessor;
import com.hh.model.User;
import com.hh.model.UserManager;
import com.hh.msg.GameMsgProtocol;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.AttributeKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UserAttkCmdHandler implements CmdHandler<GameMsgProtocol.UserAttkCmd>{

    private static final Logger LOGGER = LoggerFactory.getLogger(UserAttkCmdHandler.class);

    @Override
    public void handle(ChannelHandlerContext ctx, GameMsgProtocol.UserAttkCmd cmd) {
        Integer userId = (Integer) ctx.channel().attr(AttributeKey.valueOf("userId")).get();
        if(userId==null){
            return;
        }
        int targetUserId = cmd.getTargetUserId();
        GameMsgProtocol.UserAttkResult.Builder resultBuilder = GameMsgProtocol.UserAttkResult.newBuilder();
        resultBuilder.setAttkUserId(userId);
        resultBuilder.setTargetUserId(targetUserId);
        GameMsgProtocol.UserAttkResult result = resultBuilder.build();
        Broadcaster.broadcast(result);

        int subtractHp = 10;
        User targetUser = UserManager.getUserById(targetUserId);
        if(targetUser==null){
            return;
        }

        LOGGER.info("当前前程：{}",Thread.currentThread().getName());
        targetUser.hp = targetUser.hp - subtractHp;
        broadcastSubtractHp(targetUserId,subtractHp);

        if(targetUser.hp <= 0){
            broadcastSubtractDie(targetUserId);
        }
    }

    private static void broadcastSubtractDie(int targetUserId) {
        GameMsgProtocol.UserDieResult.Builder resultBuilder = GameMsgProtocol.UserDieResult.newBuilder();
        resultBuilder.setTargetUserId(targetUserId);
        GameMsgProtocol.UserDieResult result = resultBuilder.build();
        Broadcaster.broadcast(result);
    }


    private static void broadcastSubtractHp(int targetUserId,int subtractHp){
        if(targetUserId <= 0 || subtractHp <= 0){
            return;
        }
        GameMsgProtocol.UserSubtractHpResult.Builder resultBuilder = GameMsgProtocol.UserSubtractHpResult.newBuilder();
        resultBuilder.setTargetUserId(targetUserId);
        resultBuilder.setSubtractHp(subtractHp);
        GameMsgProtocol.UserSubtractHpResult result = resultBuilder.build();
        Broadcaster.broadcast(result);
    }
}
