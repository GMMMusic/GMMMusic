package com.gaochlei.mulmusic.msgline;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by 高春雷 on 2017/9/18.
 */

public class MessageManager<Param,Result> implements ObserverMessage<AbsMessage,Param,Result> {
    public static final String PAUSE_FROM_SERVICE = "pauseFromService";
    private HashMap<String,Set<AbsMessage>> map;
    private final Object object = new Object();
    private static MessageManager msgManager;

    private MessageManager(){
        map = new HashMap<>();
    }
    public static MessageManager instance(){
        if(msgManager == null){
            msgManager = new MessageManager();
        }
        return msgManager;
    }
    @Override
    public void registerObserver(String name, AbsMessage observer) {
        synchronized (object){
            Set<AbsMessage> observers;
            if(!map.containsKey(name)){
                observers = new HashSet<>();
                map.put(name,observers);
            }else{
                observers = map.get(name);
            }
            observers.add(observer);
        }
    }

    @Override
    public void unregisterObserver(String name) {
        synchronized (object){
            map.remove(name);
        }
    }

    @Override
    public void unregisterObserver(AbsMessage observer) {
        synchronized (object){
            for(String key : map.keySet()){
                Set<AbsMessage> observers = map.get(key);
                observers.remove(observer);
            }
        }
    }

    @Override
    public void unregisterObserver(String name, AbsMessage observer) {
        synchronized (object){
            if(map.containsKey(name)){
                map.get(name).remove(observer);
            }
        }
    }

    @Override
    public Set<AbsMessage> getObserver(String name) {
        Set<AbsMessage> observers = null;
        synchronized(object){
            if(map.containsKey(name)){
                observers = map.get(name);
            }
        }
        return observers;
    }

    @Override
    public void clear() {
        synchronized (object) {
            map.clear();
        }
    }

    @Override
    public Result notify(String name,Param... p) {
        synchronized (object){
            if(map.containsKey(name)){
                for(AbsMessage msg : map.get(name)){
                    return (Result)msg.AcceptMessage(p);
                }
            }
        }
        return null;
    }
}
