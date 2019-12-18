package com.hh.cmdhandler;

import com.hh.Broadcaster;
import com.hh.model.User;
import com.hh.model.UserManager;
import com.hh.msg.GameMsgProtocol;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.AttributeKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WhoElseIsHereCmdHandler implements CmdHandler<GameMsgProtocol.WhoElseIsHereCmd>{

    private static final Logger LOGGER = LoggerFactory.getLogger(WhoElseIsHereCmdHandler.class);

    public void handle(ChannelHandlerContext ctx, GameMsgProtocol.WhoElseIsHereCmd msg){
        GameMsgProtocol.WhoElseIsHereResult.Builder resultBuilder = GameMsgProtocol.WhoElseIsHereResult.newBuilder();

        for (User user : UserManager.listUser()) {
            if(user==null){
                continue;
            }
            GameMsgProtocol.WhoElseIsHereResult.UserInfo.Builder userInfoBuilder = GameMsgProtocol.WhoElseIsHereResult.UserInfo.newBuilder();
            userInfoBuilder.setUserId(user.userId);
            userInfoBuilder.setHeroAvatar(user.heroAvatar);

            GameMsgProtocol.WhoElseIsHereResult.UserInfo.MoveState.Builder
                    moveResultBuilder = GameMsgProtocol.WhoElseIsHereResult.UserInfo.MoveState.newBuilder();
            moveResultBuilder.setFromPosX(user.moveState.fromPosX);
            moveResultBuilder.setFromPosY(user.moveState.fromPosY);
            moveResultBuilder.setToPosX(user.moveState.toPosX);
            moveResultBuilder.setToPosY(user.moveState.toPosY);
            moveResultBuilder.setStartTime(user.moveState.stateTime);
            userInfoBuilder.setMoveState(moveResultBuilder);
            resultBuilder.addUserInfo(userInfoBuilder.build());
        }

        GameMsgProtocol.WhoElseIsHereResult result = resultBuilder.build();
        ctx.writeAndFlush(result);
    }


}
