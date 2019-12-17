package com.hh.cmdhandler;

import com.hh.Broadcaster;
import com.hh.model.User;
import com.hh.model.UserManager;
import com.hh.msg.GameMsgProtocol;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.AttributeKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UserEntryCmdHandler implements CmdHandler<GameMsgProtocol.UserEntryCmd>{

    private static final Logger LOGGER = LoggerFactory.getLogger(UserEntryCmdHandler.class);

    @Override
    public void handle(ChannelHandlerContext ctx, GameMsgProtocol.UserEntryCmd cmd){
        LOGGER.info("登录用户{}",cmd);
        int userId = cmd.getUserId();
        String heroAvatar = cmd.getHeroAvatar();

        User user = new User();
        user.userId = userId;
        user.heroAvatar = heroAvatar;
        user.hp = 100;
        UserManager.addUser(user);

        ctx.channel().attr(AttributeKey.valueOf("userId")).set(userId);

        GameMsgProtocol.UserEntryResult.Builder resultBuilder = GameMsgProtocol.UserEntryResult.newBuilder();
        resultBuilder.setUserId(userId);
        resultBuilder.setHeroAvatar(heroAvatar);

        GameMsgProtocol.UserEntryResult result = resultBuilder.build();
        Broadcaster.broadcast(result);
    }


}
