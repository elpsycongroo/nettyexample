package com.dominic.netty;

import com.dominic.netty.handler.ClientHandler;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

public class Client {

    public static void main(String[] args) throws Exception {
        // 客户端只需要一个事件循环组
        NioEventLoopGroup group = new NioEventLoopGroup();
        // 客户端使用的是BootStrap
        Bootstrap bootstrap = new Bootstrap();
        try {
            bootstrap.group(group)
                    // 设置通道实现类
                    .channel(NioSocketChannel.class)
                    // 设置handler
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            // 加入自己的处理器
                            ch.pipeline().addLast(new ClientHandler());
                        }
                    });
            System.out.println("客户端 is Ready...");
            // 启动客户端连接服务器
            ChannelFuture channelFuture = bootstrap.connect("localhost", 6668).sync();
            //监听
            channelFuture.channel().closeFuture().sync();
        } finally {
            group.shutdownGracefully();
        }
    }

}
