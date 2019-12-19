package com.hh.cmdhandler;

import com.hh.login.LoginService;
import com.hh.login.db.UserEntity;
import com.hh.model.User;
import com.hh.model.UserManager;
import com.hh.msg.GameMsgProtocol;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.AttributeKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UserLoginCmdHandler implements CmdHandler<GameMsgProtocol.UserLoginCmd> {

    private static Logger LOGGER = LoggerFactory.getLogger(UserLoginCmdHandler.class);

    @Override
    public void handle(ChannelHandlerContext ctx, GameMsgProtocol.UserLoginCmd cmd) {
        if(ctx==null || cmd==null){
            return;
        }
        String userName = cmd.getUserName();
        String password = cmd.getPassword();

        UserEntity userEntity = null;
        try {
            userEntity = LoginService.getInstance().login(userName, password);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(),e);
        }
        if(userEntity != null){

            User user = new User();
            user.hp = 100;
            user.userId = userEntity.getUserId();
            user.userName = userEntity.getUserName();
            user.heroAvatar = userEntity.getHeroAvatar();

            UserManager.addUser(user);
            ctx.channel().attr(AttributeKey.valueOf("userId")).set(user.userId);

            GameMsgProtocol.UserLoginResult.Builder resultBuilder = GameMsgProtocol.UserLoginResult.newBuilder();
            resultBuilder.setUserId(userEntity.getUserId());
            resultBuilder.setUserName(userName);
            resultBuilder.setHeroAvatar(userEntity.getHeroAvatar());
            GameMsgProtocol.UserLoginResult result = resultBuilder.build();
            ctx.writeAndFlush(result);
        }


    }
}
