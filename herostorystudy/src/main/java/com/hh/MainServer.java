package com.hh;

import com.hh.cmdhandler.CmdHandlerFactory;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MainServer {
    private static final Logger LOGGER = LoggerFactory.getLogger(MainServer.class);

    public static void main(String[] args) {
        GameMsgRecognizer.init();
        CmdHandlerFactory.init();
        MySqlSessionFactory.init();

        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workGroup = new NioEventLoopGroup();


        ServerBootstrap b = new ServerBootstrap();

        b.group(bossGroup,workGroup);
        b.channel(NioServerSocketChannel.class);


        b.childHandler(new ChannelInitializer<SocketChannel>() {
            @Override
            protected void initChannel(SocketChannel ch) throws Exception {
                ch.pipeline().addLast(
                        new HttpServerCodec(),
                        new HttpObjectAggregator(65535),
                        new WebSocketServerProtocolHandler("/websocket"),
                        new GameMsgDecoder(),
                        new GameMsgEncoder(),
                        new GameMsgHandler()
                );
            }
        });


        try{
            ChannelFuture f = b.bind(12345).sync();


            if(f.isSuccess()){
                LOGGER.info("服务器启动成功");
            }

            f.channel().closeFuture().sync();
        }catch (Exception e) {
            e.printStackTrace();
        }






    }



}
