package com.gaochlei.mulmusic.msgline;

/**
 * Created by 高春雷 on 2017/9/17.
 */

public interface  AbsMessage<Result,Param> {
    Result AcceptMessage(Param... data);
}
