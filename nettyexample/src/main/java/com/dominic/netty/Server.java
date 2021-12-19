package com.dominic.netty;

import com.dominic.netty.handler.ServerHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

public class Server {

    public static void main(String[] args) throws Exception {
        // 创建BOSS和workerGroup
        // bossGroup只处理连接请求 真正的和客户端业务处理会交给workerGroup
        // 两个都是无限循环
        // bossGroup和workerGroup默认的线程数是CPU的核数*2
        NioEventLoopGroup bossGroup = new NioEventLoopGroup();
        NioEventLoopGroup workerGroup = new NioEventLoopGroup();

        // 创建服务器端的启动对象，配置参数
        ServerBootstrap bootstrap = new ServerBootstrap();
        try {
            // 进行配置
            // 设置两个线程组
            bootstrap.group(bossGroup, workerGroup)
                    // 使用NIOServerSocketChannel作为服务器的通道实现
                    .channel(NioServerSocketChannel.class)
                    // 设置线程队列得到连接个数
                    .option(ChannelOption.SO_BACKLOG, 128)
                    // 设置保持活动连接状态
                    .childOption(ChannelOption.SO_KEEPALIVE, true)
                    // 给workerGroup的EventLoop对应的管道设置处理器
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            // 创建一个通道初始化
                            ch.pipeline().addLast(new ServerHandler());
                        }
                    });
            System.out.println("--------Server is Ready");
            // 绑定一个端口并且同步 生成了一个channelFuture对象
            // 启动服务器
            ChannelFuture cf = bootstrap.bind(6668).sync();
            // 对关闭通道进行监听
            cf.channel().closeFuture().sync();
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }


}
