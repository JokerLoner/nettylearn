package com.keniang.netty.handler;

import com.keniang.netty.entry.UnixTime;
import io.netty.buffer.ByteBuf;

import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;


/**
 * Created by thinkpad on 2017/7/16.
 */
public class TimeServerHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelActive(final ChannelHandlerContext ctx) throws Exception { // 1 将在连接被建立并且准备通信的时候调用
//        final ByteBuf time = ctx.alloc().buffer(4); // 2 写入一个32位的整数，这里用至少4个字节的Bytebuf
//        time.writeInt((int)(System.currentTimeMillis()/1000L + 220898880L));
//
//        final ChannelFuture f = ctx.writeAndFlush(time); // 3 write和writeAndFlush 方法会返回一个ChannelFuture对象 一个ChannelFuture代表未发生的IO操作。意味着任何一个请求都不会马上执行
//        f.addListener(new ChannelFutureListener() {
//            public void operationComplete(ChannelFuture future) throws Exception {
//                assert f == future;
//                ctx.close();
//            }
//        }); //4  监听一个ChannelFuture 这里在他完成后，关闭Channel
        // f.addListener(ChannelFutureListener.CLOSE);  // 或者使用该预定义监听器
       ChannelFuture f = ctx.writeAndFlush(new UnixTime());
       f.addListener(ChannelFutureListener.CLOSE);

    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
