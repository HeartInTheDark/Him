package com.hitd.im.tcp.server;

/**
 * @Author ZhangWeinan
 * @Date 2023/3/14 19:16
 * @DES
 * @Since Copyright(c)
 */

public interface HimServer {
    void start();
    void shutdownGracefully();
}
