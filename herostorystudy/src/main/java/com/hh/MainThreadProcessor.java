package com.hh;

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

    public void process(Runnable runnable){
        try {
            ES.submit(runnable);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(),e);
        }
    }

}
