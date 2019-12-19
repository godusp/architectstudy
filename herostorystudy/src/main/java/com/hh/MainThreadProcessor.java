package com.hh;

import com.google.protobuf.GeneratedMessageV3;
import com.hh.cmdhandler.CmdHandler;
import com.hh.cmdhandler.CmdHandlerFactory;
import io.netty.channel.ChannelHandlerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainThreadProcessor {

    private MainThreadProcessor(){}

    private static Logger LOGGER = LoggerFactory.getLogger(MainThreadProcessor.class);

    private static MainThreadProcessor INSTANCE = new MainThreadProcessor();

    private static ExecutorService ES = Executors.newSingleThreadExecutor((runnable)->{
        Thread thread = new Thread(runnable);
        thread.setName("MainThreadProcessor");
        return thread;
    });


    public static MainThreadProcessor getInstance(){
        return INSTANCE;
    }

    public void process(ChannelHandlerContext ctx, GeneratedMessageV3 msg){
        ES.submit(()->{
            CmdHandler<? extends GeneratedMessageV3> cmdHandler = CmdHandlerFactory.getCmdHanlder(msg.getClass());
            if(cmdHandler==null){
                LOGGER.error("未找到对应的指令处理器,msgClass = {}" , msg.getClass().getName());
                return;
            }
            try {
                cmdHandler.handle(ctx,cast(msg));
            } catch (Exception e) {
                LOGGER.error(e.getMessage(),e);
            }
        });
    }

    private static <T extends GeneratedMessageV3> T cast(Object msg){
        if(msg==null){
            return null;
        } else {
            return (T) msg;
        }
    }
}
