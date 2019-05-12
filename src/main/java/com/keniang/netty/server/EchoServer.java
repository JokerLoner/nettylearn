package com.keniang.netty.server;

import com.keniang.netty.handler.EchoServerHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.CompositeByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.Random;

/**
 * Created by thinkpad on 2017/8/5.
 */
public class EchoServer {
    private final int port;

    public EchoServer(int port) {
        this.port = port;
    }

    public void start() throws Exception {
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(group).channel(NioServerSocketChannel.class).localAddress(port).childHandler(new ChannelInitializer<Channel>() {
                @Override
                protected void initChannel(Channel ch) throws Exception {
                    ch.pipeline().addLast(new EchoServerHandler());
                }
            });
            ChannelFuture f = b.bind().sync();
            System.out.println(EchoServer.class.getName() + " started and listen on " + f.channel().localAddress());
            f.channel().closeFuture().sync();
        } finally {
            group.shutdownGracefully().sync();
        }
    }

    public static void main(String[] args) throws  Exception {
//        new EchoServer(65535).start();
//        ByteBuf buf = Unpooled.directBuffer(16);
//        if (!buf.hasArray()) {
//            int len = buf.readableBytes();
//            byte[] arr = new byte[len];
//            buf.getBytes(0, arr);
//        }

//        CompositeByteBuf compBuf = Unpooled.compositeBuffer();
//        ByteBuf headBuf = Unpooled.buffer(8);
//        ByteBuf directBuf = Unpooled.directBuffer(8);
//
//        compBuf.addComponents(headBuf,directBuf);
//        compBuf.removeComponent(0);
//        Iterator<ByteBuf> iter = compBuf.iterator();
//
//        while(iter.hasNext()) {
//            System.out.println(iter.next().toString());
//        }
//
//        if (!compBuf.hasArray()) {
//            int len = compBuf.readableBytes();
//            byte[] arr = new byte[len];
//            compBuf.getBytes(0, arr);
//        }
//
//        ByteBuf readBuf = Unpooled.buffer(16);
//        while(readBuf.isReadable()) {
//            System.out.println(readBuf.readByte());
//        }
//
//        Random random = new Random();
//        ByteBuf writeBuf = Unpooled.buffer(16);
//        while (writeBuf.writableBytes() >= 4) {
//            writeBuf.writeInt(random.nextInt());
//        }
        Charset utf8 = Charset.forName("UTF-8");
        ByteBuf buf = Unpooled.copiedBuffer("Netty in action", utf8);
        ByteBuf slice = buf.slice(0,14);

        ByteBuf copied = buf.copy(0, 14);
        System.out.println(buf.toString(utf8));
        System.out.println(slice.toString(utf8));
        System.out.println(copied.toString(utf8));
    }
}
