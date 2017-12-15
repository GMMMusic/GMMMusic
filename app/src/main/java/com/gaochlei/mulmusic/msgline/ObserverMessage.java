package com.gaochlei.mulmusic.msgline;

import java.util.Set;

/**
 * Created by 高春雷 on 2017/9/18.
 * 观察者模式，主题接口
 * @param <T> 主题
 * @param <P> 参数
 * @param <R> 返回值
 */

public interface ObserverMessage<T,P,R> {
    /**
     * 根据名称注册观察者
     * @param name
     * @param observer
     */
    void registerObserver(String name,T observer);

    /**
     * 根据名称反注册观察者
     * @param name
     */
    void unregisterObserver(String name);

    /**
     * 根据主题反注册观察者
     * @param observer
     */
    void unregisterObserver(T observer);

    /**
     * 根据主题，名称反注册观察者
     * @param name
     * @param observer
     */
    void unregisterObserver(String name,T observer);

    /**
     * 根据名称 获取观察者
     * @param name
     * @return T
     */
    Set<T> getObserver(String name);

    /**
     * 清除观察者
     */
    void clear();

    /**
     * 通知观察者
     * @param p
     * @return
     */
    R notify(String name,P... p);

}
