package com.hitd.im.tcp.server;

import com.hitd.im.eodec.config.BootstrapConfig;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.stream.ChunkedWriteHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @Author ZhangWeinan
 * @Date 2023/3/14 18:25
 * @DES
 * @Since Copyright(c)
 */

public class HimWebSocketServer implements HimServer{
    private final Logger logger = LoggerFactory.getLogger(HimTcpServer.class);

    private final BootstrapConfig.Him bootstrapConfig;
    private final EventLoopGroup bossGroup;
    private final EventLoopGroup workGroup;
    private final ServerBootstrap serverBootstrap;

    public HimWebSocketServer(BootstrapConfig.Him config) {
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
                        ChannelPipeline pipeline = ch.pipeline();
                        // websocket 基于http协议，所以要有http编解码器
                        pipeline.addLast("http-codec", new HttpServerCodec());
                        // 对写大数据流的支持
                        pipeline.addLast("http-chunked", new ChunkedWriteHandler());
                        // 几乎在netty中的编程，都会使用到此handler
                        pipeline.addLast("aggregator", new HttpObjectAggregator(65535));
                        /**
                         * websocket 服务器处理的协议，用于指定给客户端连接访问的路由 : /ws
                         * 此handler会帮你处理一些繁重的复杂的事
                         * 会帮你处理握手动作： handshaking（close, ping, pong） ping + pong = 心跳
                         * 对于websocket来讲，都是以frames进行传输的，不同的数据类型对应的frames也不同
                         */
                        pipeline.addLast(new WebSocketServerProtocolHandler("/ws"));
                    }
                });
    }

    public void start(){
        this.serverBootstrap.bind(bootstrapConfig.getWebsocketPort());
        logger.info("websocket start...");
    }

    public void shutdownGracefully(){
        bossGroup.shutdownGracefully();
        workGroup.shutdownGracefully();
    }
}
