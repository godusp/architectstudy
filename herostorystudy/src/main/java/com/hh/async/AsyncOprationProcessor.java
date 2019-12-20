package com.hh.async;

import com.hh.MainThreadProcessor;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public final class AsyncOprationProcessor {
    private static AsyncOprationProcessor INSTANCE = new AsyncOprationProcessor();

    private static final ExecutorService[] ES_ARRAY = new ExecutorService[8];

    static {
        for (int i = 0; i < ES_ARRAY.length; i++) {
            final String threadName = "AsyncOprationProcessor" + i;
            ES_ARRAY[i] = Executors.newSingleThreadExecutor((runnable)->{
                Thread thread = new Thread(runnable);
                thread.setName(threadName);
                return thread;
            });
        }
    }



    public static AsyncOprationProcessor getInstance() {
        return INSTANCE;
    }

    private AsyncOprationProcessor(){
    }

    public void process(AsyncOperation asyncOperation){
        int index = asyncOperation.bindId() % 8;
        ES_ARRAY[index].submit(()->{
            asyncOperation.doAsync();
            MainThreadProcessor.getInstance().process(asyncOperation::doFinish);
        });
    }
}
