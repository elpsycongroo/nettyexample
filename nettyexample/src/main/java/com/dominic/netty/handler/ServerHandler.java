package com.dominic.netty.handler;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;

public class ServerHandler extends ChannelInboundHandlerAdapter {
    /*
    * 自定义一个handler 继承netty规定好的某个HandlerAdapter
    * 支持才能称为一个handler
    */

    /**
     * 读取数据（可以读取客户端发送的消息）
     * @param ctx 上下文对象 含有管道pipeline 、通道channel 、地址
     * @param msg 客户端发送的数据 默认是object
     * @throws Exception
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.out.println("server ctx = " + ctx);
        // 将msg转为一个byteBuf
        ByteBuf buf = (ByteBuf) msg;
        System.out.println("客户端发送消息： " + buf.toString(CharsetUtil.UTF_8));
        System.out.println("客户端地址： " + ctx.channel().remoteAddress());
    }

    /**
     * 数据读取完毕
     * @param ctx 上下文
     * @throws Exception
     */
    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        // write + flush 写入并刷新
        // 我们对发送数据进行编码
        ctx.writeAndFlush(Unpooled.copiedBuffer("hello, 客户端~", CharsetUtil.UTF_8));
    }

    /**
     * 处理异常
     * @param ctx 上下文
     * @param cause
     * @throws Exception
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.channel().close();
    }
}
