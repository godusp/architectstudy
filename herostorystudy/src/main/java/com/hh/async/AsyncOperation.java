package com.hh.async;

@FunctionalInterface
public interface AsyncOperation {
    default int bindId(){
        return 1;
    }
    void doAsync();
    default void doFinish(){
    }
}
