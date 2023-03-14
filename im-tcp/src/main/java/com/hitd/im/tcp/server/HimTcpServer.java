package com.hitd.im.tcp.server;

import com.hitd.im.eodec.config.BootstrapConfig;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @Author ZhangWeinan
 * @Date 2023/3/14 18:00
 * @DES
 * @Since Copyright(c)
 */

public class HimTcpServer implements HimServer{

    private final Logger logger = LoggerFactory.getLogger(HimTcpServer.class);

    private final BootstrapConfig.Him bootstrapConfig;
    private final EventLoopGroup bossGroup;
    private final EventLoopGroup workGroup;
    private final ServerBootstrap serverBootstrap;


    public HimTcpServer(BootstrapConfig.Him config) {
        this.bootstrapConfig = config;
        bossGroup = new NioEventLoopGroup(bootstrapConfig.getBossThreadSize());
        workGroup = new NioEventLoopGroup(bootstrapConfig.getWorkThreadSize());

        serverBootstrap = new ServerBootstrap();
        serverBootstrap.group(bossGroup, workGroup)
                .channel(NioServerSocketChannel.class)
                .option(ChannelOption.SO_BACKLOG, bootstrapConfig.getConnectionQueue()) //服务端可连接队列大小
                .option(ChannelOption.SO_REUSEADDR, true)//表示可以重复使用本地IP和端口
                .childOption(ChannelOption.TCP_NODELAY, true) //禁用Nagle算法
                .childOption(ChannelOption.SO_KEEPALIVE, true)//2h没有数据服务端会发送心跳包
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {

                    }
                });
    }

    @Override
    public void start(){
        this.serverBootstrap.bind(bootstrapConfig.getTcpPort());
        logger.info("tcpserver start...");
    }

    @Override
    public void shutdownGracefully(){
        bossGroup.shutdownGracefully();
        workGroup.shutdownGracefully();
    }
}
